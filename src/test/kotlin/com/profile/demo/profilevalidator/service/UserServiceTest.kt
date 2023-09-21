package com.profile.demo.profilevalidator.service

import com.profile.demo.profilevalidator.client.ValidatorApiClient
import com.profile.demo.profilevalidator.exception.ValidationException
import com.profile.demo.profilevalidator.dto.AddOrUpdateUserRequest
import com.profile.demo.profilevalidator.model.User
import com.profile.demo.profilevalidator.model.UserAddress
import com.profile.demo.profilevalidator.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var validatorClient: ValidatorApiClient

    @Mock
    private lateinit var subscriptionService: SubscriptionService

    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        userService = UserService(userRepository, validatorClient, subscriptionService)
    }

    @Test
    fun `getUser should return user if found`() {

        val userId = "user123"
        val user = User(id = userId, legalName = "Legal Name")
        `when`(userRepository.findById(userId)).thenReturn(user)

        val result = userService.getUser(userId)

        assert(result == user)
    }

    @Test
    fun `getUser should return null if user not found`() {
        val userId = "nonexistentUser"
        `when`(userRepository.findById(userId)).thenReturn(null)

        val result = userService.getUser(userId)

        assert(result == null)
    }

    @Test
    fun `addOrUpdateUser should add a new user`() {

        val request = AddOrUpdateUserRequest(
            legalName = "Legal name",
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
        `when`(userRepository.findById(anyString())).thenReturn(null)
        doNothing().`when`(userRepository).save(any(User::class.java) ?: User())
        `when`(subscriptionService.getSubscribedProducts(anyString())).thenReturn(listOf("product1", "product2"))
        doReturn(true).`when`(validatorClient).validate(anyString(), any(User::class.java) ?: User())

        userService.addOrUpdateUser(request)
    }

    @Test
    fun `addOrUpdateUser should update an existing user`() {

        val userId = "user123"
        val existingUser = User(id = userId, legalName = "User Legal")
        val request = AddOrUpdateUserRequest(
            id = userId,
            legalName = "Updated User Legal",
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

        `when`(userRepository.findById(userId)).thenReturn(existingUser)
        doNothing().`when`(userRepository).save(any(User::class.java) ?: User())
        `when`(subscriptionService.getSubscribedProducts(anyString())).thenReturn(listOf("product1", "product2"))
        doReturn(true).`when`(validatorClient).validate(anyString(), any(User::class.java) ?: User())

        userService.addOrUpdateUser(request)
    }

    @Test
    fun `addOrUpdateUser should throw ValidationException when user is not validated by subscribed products`() {
        val request = AddOrUpdateUserRequest(
            legalName = "Legal Name",
            companyName = "Acme Inc.",
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
        `when`(userRepository.findById(anyString())).thenReturn(null)
        `when`(userRepository.save(any(User::class.java) ?: User())).thenAnswer {it.arguments[0]}
        `when`(subscriptionService.getSubscribedProducts(anyString())).thenReturn(listOf("product1", "product2"))
        `when`(validatorClient.validate(anyString(), any(User::class.java) ?: User())).thenReturn(false)

        assertThrows<ValidationException> {
            userService.addOrUpdateUser(request)
        }
    }

    @Test
    fun `deleteUser should delete user when user exists`() {
        val userId = "user123"
        doNothing().`when`(userRepository).deleteById(userId)

        userService.deleteUser(userId)
    }

    @Test
    fun `deleteUser should throw RuntimeException when user deletion fails`() {
        val userId = "user123"
        doThrow(RuntimeException("Deletion failed")).`when`(userRepository).deleteById(userId)

        assertThrows<RuntimeException> {
            userService.deleteUser(userId)
        }
    }
}
