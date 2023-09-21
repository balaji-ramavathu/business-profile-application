package com.profile.demo.profilevalidator.exception

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class CustomExceptionHandlerTest {

    private val customExceptionHandler = CustomExceptionHandler()

    @Test
    fun testHandleValidationException() {
        val validationException = ValidationException("Validation failed")
        val responseEntity: ResponseEntity<ErrorResponse> = customExceptionHandler.handleValidationException(validationException)

         assertEquals(HttpStatus.BAD_REQUEST, responseEntity.statusCode)
         assertNotNull(responseEntity.body)
         assertEquals("Validation failed", responseEntity.body?.message)
    }
}
