package com.profile.demo.profilevalidator.service

import com.profile.demo.profilevalidator.dto.AddOrUpdateProductRequest
import com.profile.demo.profilevalidator.model.Product
import com.profile.demo.profilevalidator.repository.ProductRepository
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class ProductServiceTest {

    @Mock
    private lateinit var productRepository: ProductRepository

    @InjectMocks
    private lateinit var productService: ProductService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testGetProduct_Success() {
        val productId = "1"
        val product = Product(id = productId, name = "Product 1", type = "PAYMENTS")

        Mockito.`when`(productRepository.findById(productId)).thenReturn(product)

        val result = productService.getProduct(productId)

        assertNotNull(result)
        assertEquals(productId, result?.id)
    }

    @Test
    fun testGetProduct_NotFound() {
        val productId = "2"

        Mockito.`when`(productRepository.findById(productId)).thenReturn(null)

        val result = productService.getProduct(productId)

        assertNull(result)
    }

    @Test
    fun testAddOrUpdateProduct_AddNew() {
        val request = AddOrUpdateProductRequest(
            id = "NewProduct",
            name = "New Product",
            type = "LENDING"
        )

        productService.addOrUpdateProduct(request)

        Mockito.verify(productRepository).save(Mockito.any(Product::class.java) ?: Product())
    }

    @Test
    fun testAddOrUpdateProduct_UpdateExisting() {
        val existingProductId = "3"
        val existingProduct = Product(id = existingProductId, name = "Existing Product", type = "INVESTMENT")
        val request = AddOrUpdateProductRequest(
            id = existingProductId,
            name = "Updated Product",
            type = "INSURANCE"
        )

        Mockito.`when`(productRepository.findById(existingProductId)).thenReturn(existingProduct)

        productService.addOrUpdateProduct(request)

        Mockito.verify(productRepository).save(Mockito.any(Product::class.java) ?: Product())
    }

    @Test
    fun testDeleteProduct() {
        val productId = "4"

        productService.deleteProduct(productId)

        Mockito.verify(productRepository).deleteById(productId)
    }
}
