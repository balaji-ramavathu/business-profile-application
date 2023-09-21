package com.profile.demo.profilevalidator.controller

import com.profile.demo.profilevalidator.dto.AddOrUpdateProductRequest
import com.profile.demo.profilevalidator.model.Product
import com.profile.demo.profilevalidator.service.ProductService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ProductControllerTest {

    @Mock
    private lateinit var productService: ProductService

    @InjectMocks
    private lateinit var productController: ProductController

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testGetProduct_Success() {
        
        val productId = "123"
        val product = Product(id = productId, name = "Test Product")
        `when`(productService.getProduct(productId)).thenReturn(product)

        val response: ResponseEntity<Product?> = productController.getProduct(productId)

        verify(productService).getProduct(productId)
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body == product)
    }

    @Test
    fun testGetProduct_NotFound() {
        
        val productId = "456"
        `when`(productService.getProduct(productId)).thenReturn(null)

        val response: ResponseEntity<Product?> = productController.getProduct(productId)
        
        verify(productService).getProduct(productId)
        assert(response.statusCode == HttpStatus.NOT_FOUND)
        assert(response.body == null)
    }

    @Test
    fun testAddOrUpdateProduct_Success() {
        
        val request = AddOrUpdateProductRequest(
            id = "NewProduct",
            name = "New Product",
            type = "PAYMENTS")

        val responseEntity = productController.addOrUpdateProduct(request)

        verify(productService).addOrUpdateProduct(request)
        assert(responseEntity.statusCode == HttpStatus.OK)
    }

    @Test
    fun testDeleteProduct_Success() {
        
        val productId = "789"

        val responseEntity = productController.deleteProduct(productId)

        verify(productService).deleteProduct(productId)
        assert(responseEntity.statusCode == HttpStatus.OK)
    }

    @Test
    fun testDeleteProduct_NotFound() {
        
        val productId = "999"
        doThrow(NoSuchElementException("Product not found")).`when`(productService).deleteProduct(productId)

        val responseEntity = productController.deleteProduct(productId)
        
        verify(productService).deleteProduct(productId)
        assert(responseEntity.statusCode == HttpStatus.NOT_FOUND)
    }
}
