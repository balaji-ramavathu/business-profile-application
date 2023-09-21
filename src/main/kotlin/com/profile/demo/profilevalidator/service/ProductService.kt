package com.profile.demo.profilevalidator.service

import com.profile.demo.profilevalidator.dto.AddOrUpdateProductRequest
import com.profile.demo.profilevalidator.model.Product
import com.profile.demo.profilevalidator.repository.ProductRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductService @Autowired constructor(private val productRepository: ProductRepository) {
    private val logger by lazy { LoggerFactory.getLogger(ProductService::class.java) }

    fun getProduct(productId: String): Product? {
        logger.info("Fetching product with ID: $productId")

        return productRepository.findById(productId)
    }

    fun addOrUpdateProduct(request: AddOrUpdateProductRequest) {
        logger.info("Adding or updating product")

        val product = (
                productRepository.findById(request.id)
                    ?: Product(
                        id = request.id,
                        addedAt = System.currentTimeMillis()
                    ))
            .copy(
                name = request.name,
                type = request.type,
                updatedAt = System.currentTimeMillis()
        )
        productRepository.save(product)

        logger.info("Added / updated product successfully")
    }

    fun deleteProduct(productId: String) {
        logger.info("Deleting product with ID: $productId")

        val result = kotlin.runCatching {
            productRepository.deleteById(productId)
        }

        if (result.isFailure) {
            logger.error("Failed to delete product with ID $productId", result.exceptionOrNull())
            throw RuntimeException("Failed to delete product with ID $productId", result.exceptionOrNull())
        }

        logger.info("Product with ID $productId deleted successfully")
    }
}