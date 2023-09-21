package com.profile.demo.profilevalidator.controller

import com.profile.demo.profilevalidator.dto.AddOrUpdateProductRequest
import com.profile.demo.profilevalidator.model.Product
import com.profile.demo.profilevalidator.service.ProductService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["http://localhost:3000"])
@RequestMapping("/product")
class ProductController @Autowired constructor(private val productService: ProductService) {

    private val logger by lazy { LoggerFactory.getLogger(ProductController::class.java) }

    @GetMapping("/get/{productId}")
    fun getProduct(@PathVariable productId: String): ResponseEntity<Product?> {
        logger.info("Received GET request to retrieve product with ID: $productId")

        val product = productService.getProduct(productId)

        return if (product == null) {
            logger.warn("Product with ID $productId not found")
            ResponseEntity.notFound().build()
        } else {
            logger.info("Product with ID $productId found and retrieved successfully")
            ResponseEntity.ok(product)
        }
    }

    @PostMapping("/add-or-update")
    fun addOrUpdateProduct(@RequestBody request: AddOrUpdateProductRequest): ResponseEntity<Void> {
        logger.info("Received POST request to add/update product ${request.id}")

        return try {
            productService.addOrUpdateProduct(request)
            logger.info("Product with ID ${request.id} updated successfully")
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            logger.error("Internal server error while adding/updating product: ${e.message}")
            ResponseEntity.internalServerError().build()
        }
    }

    @DeleteMapping("/delete/{productId}")
    fun deleteProduct(@PathVariable productId: String): ResponseEntity<Void> {
        logger.info("Received DELETE request to delete product with ID: $productId")

        return try {
            productService.deleteProduct(productId)
            logger.info("Product with ID $productId deleted successfully")
            ResponseEntity.ok().build()
        } catch (e: NoSuchElementException) {
            logger.warn("Product with ID $productId not found")
            ResponseEntity.notFound().build()
        } catch (e: Exception) {
            logger.error("Internal server error while deleting product with ID $productId")
            ResponseEntity.internalServerError().build()
        }
    }
}