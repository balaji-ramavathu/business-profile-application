package com.profile.demo.profilevalidator.repository

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.profile.demo.profilevalidator.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Repository

@Repository
class UserRepository @Autowired constructor(@Lazy private val dynamoDBMapper: DynamoDBMapper) {

    fun save(user: User) {
        dynamoDBMapper.save(user)
    }

    fun findById(userId: String): User? {
        return dynamoDBMapper.load(User::class.java, userId)
    }

    fun deleteById(userId: String) {
        val user = findById(userId)
        if (user != null) {
            dynamoDBMapper.delete(user)
        }
    }
}