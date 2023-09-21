package com.profile.demo.profilevalidator.controller

import com.profile.demo.profilevalidator.exception.ValidationException
import com.profile.demo.profilevalidator.dto.AddOrUpdateUserRequest
import com.profile.demo.profilevalidator.model.User
import com.profile.demo.profilevalidator.model.UserAddress
import com.profile.demo.profilevalidator.service.UserService
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class UserControllerTest {
    @Mock
    private lateinit var userService: UserService

    @InjectMocks
    private lateinit var userController: UserController

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testGetUser_Success() {
        val userId = "123"
        val testUser = User()
        `when`(userService.getUser(userId)).thenReturn(testUser)

        val response: ResponseEntity<User?> = userController.getUser(userId)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(testUser, response.body)
    }

    @Test
    fun testGetUser_NotFound() {
        val userId = "nonExistentUser"
        `when`(userService.getUser(userId)).thenReturn(null)

        val response: ResponseEntity<User?> = userController.getUser(userId)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
    }

    @Test
    fun testAddOrUpdateUser_Success() = runBlocking {
        val request = AddOrUpdateUserRequest(
            legalName = "Legal Name",
            companyName = "Company Name",
            businessAddress = UserAddress(
                line1 = "Business Line 1",
                line2 = "Business Line 2",
                city = "Business City",
                state = "Business State",
                zip = 235609,
                country = "Business Country"
            ),
            legalAddress = UserAddress(
                line1 = "Legal Line 1",
                line2 = "Legal Line 2",
                city = "Legal City",
                state = "Legal State",
                zip = 238008,
                country = "Legal Country"
            ),
            taxId = "123456789",
            email = "user@example.com",
            website = "https://example.com"
        )

        doAnswer {
            // do nothing
        }.`when`(userService).addOrUpdateUser(request)

        val response: ResponseEntity<String> = userController.addOrUpdateUser(request)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }

    @Test
    fun testAddOrUpdateUser_ValidationFailure(): Unit = runBlocking {
        val invalidRequest = AddOrUpdateUserRequest(
            id = "USR06f90da3-d0ff-42c3-9431-716978a1ee5b",
            legalName = "Legal Name",
            companyName = "Company Name",
            businessAddress = UserAddress(
                line1 = "Business Line 1",
                line2 = "Business Line 2",
                city = "Business City",
                state = "Business State",
                zip = 235609,
                country = "Business Country"
            ),
            legalAddress = UserAddress(
                line1 = "Legal Line 1",
                line2 = "Legal Line 2",
                city = "Legal City",
                state = "Legal State",
                zip = 238008,
                country = "Legal Country"
            ),
            taxId = "123456789",
            email = "",     // passing blank email to trigger invalid request
            website = "https://example.com"
        )

        doThrow(ValidationException("User is not validated by one or more of the subscribed products"))
            .`when`(userService).addOrUpdateUser(invalidRequest)

        val response = userController.addOrUpdateUser(invalidRequest)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("User is not validated by one or more of the subscribed products", response.body)
    }

    @Test
    fun testAddOrUpdateUser_ExceptionThrown() = runBlocking {
        val request = AddOrUpdateUserRequest(
            legalName = "Legal Name",
            companyName = "Company Name",
            businessAddress = UserAddress(
                line1 = "Business Line 1",
                line2 = "Business Line 2",
                city = "Business City",
                state = "Business State",
                zip = 235609,
                country = "Business Country"
            ),
            legalAddress = UserAddress(
                line1 = "Legal Line 1",
                line2 = "Legal Line 2",
                city = "Legal City",
                state = "Legal State",
                zip = 238008,
                country = "Legal Country"
            ),
            taxId = "123456789",
            email = "user@example.com",
            website = "https://example.com"
        )

        doThrow(RuntimeException("Something went wrong")).`when`(userService).addOrUpdateUser(request)

        val response: ResponseEntity<String> = userController.addOrUpdateUser(request)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
    }


    @Test
    fun testDeleteUser() {
        val userId = "123"
        doNothing().`when`(userService).deleteUser(userId)

        val response: ResponseEntity<Void> = userController.deleteUser(userId)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }

    @Test
    fun testDeleteUser_ExceptionThrown() {
        val userId = "123"

        doThrow(RuntimeException("Something went wrong")).`when`(userService).deleteUser(userId)

        val response: ResponseEntity<Void> = userController.deleteUser(userId)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
    }

}
