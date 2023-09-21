package com.profile.demo.profilevalidator.service

import com.profile.demo.profilevalidator.exception.ValidationException
import com.profile.demo.profilevalidator.client.ValidatorApiClient
import com.profile.demo.profilevalidator.dto.AddOrUpdateUserRequest
import com.profile.demo.profilevalidator.model.User
import com.profile.demo.profilevalidator.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.NoSuchElementException


@Service
class UserService @Autowired constructor(
    private val userRepository: UserRepository,
    private val validatorClient: ValidatorApiClient,
    private val subscriptionService: SubscriptionService
) {
    private val logger by lazy { LoggerFactory.getLogger(UserService::class.java) }


    fun getUser(userId: String): User? {
        logger.info("Fetching user with ID: $userId")
        return userRepository.findById(userId)
    }

    fun addOrUpdateUser(request: AddOrUpdateUserRequest) = runBlocking {
        logger.info("Adding or updating user")

        val user = if (request.id == null) {
            User(
                id = "USR${UUID.randomUUID()}",
                legalName = request.legalName,
                companyName = request.companyName,
                businessAddress = request.businessAddress,
                legalAddress = request.businessAddress,
                taxId = request.taxId,
                email = request.email,
                website = request.website,
                addedAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        } else {
            userRepository.findById(request.id)?.copy(
                legalName = request.legalName,
                companyName = request.companyName,
                businessAddress = request.businessAddress,
                legalAddress = request.legalAddress,
                taxId = request.taxId,
                email = request.email,
                website = request.website,
                updatedAt = System.currentTimeMillis()
            ) ?: throw NoSuchElementException("User with ID ${request.id} doesn't exist")
        }

        if (!validateUser(user)) {
            logger.error("User is not validated by one or more of the subscribed products")
            throw ValidationException("User is not validated by one or more of the subscribed products")
        }

        userRepository.save(user)
        logger.info("User added/updated successfully")
    }

    private suspend fun validateUser(user: User) = coroutineScope {
        logger.info("Validating user ${user.id}")

        val validationResults = subscriptionService.getSubscribedProducts(user.id).map { productId ->
            async {
                ValidateUserCommand(validatorClient, productId, user).execute()            }
        }

        val allValid = validationResults.awaitAll().all { it }
        if (!allValid) {
            logger.error("User validation failed for one or more subscribed products")
        }

        allValid
    }

    fun deleteUser(userId: String) {
        logger.info("Deleting user with ID: $userId")

        val result = runCatching {
            userRepository.deleteById(userId)
        }

        if (result.isFailure) {
            logger.error("Failed to delete user with ID $userId", result.exceptionOrNull())
            throw RuntimeException("Failed to delete user with ID $userId", result.exceptionOrNull())
        }

        logger.info("User with ID $userId deleted successfully")
    }
}