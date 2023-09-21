package com.profile.demo.profilevalidator.repository

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.profile.demo.profilevalidator.model.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Repository


@Repository
class ProductRepository @Autowired constructor(@Lazy private val dynamoDBMapper: DynamoDBMapper) {

    fun save(product: Product) {
        dynamoDBMapper.save(product)
    }

    fun findById(productId: String): Product? {
        return dynamoDBMapper.load(Product::class.java, productId)
    }

    fun deleteById(productId: String) {
        val product = findById(productId)
        if (product != null) {
            dynamoDBMapper.delete(product)
        }
    }
}