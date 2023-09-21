package com.profile.demo.profilevalidator.controller

import com.netflix.hystrix.exception.HystrixRuntimeException
import com.profile.demo.profilevalidator.exception.ValidationException
import com.profile.demo.profilevalidator.dto.AddOrUpdateUserRequest
import com.profile.demo.profilevalidator.model.User
import com.profile.demo.profilevalidator.service.UserService
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.Exception


@RestController
@CrossOrigin(origins = ["http://localhost:3000"])
@RequestMapping("/user")
class UserController @Autowired constructor(private val userService: UserService) {

    private val logger by lazy { LoggerFactory.getLogger(UserController::class.java) }

    @GetMapping("/get/{userId}")
    fun getUser(@PathVariable userId: String): ResponseEntity<User?> {
        logger.info("Received GET request to retrieve user with ID: $userId")

        val user = userService.getUser(userId)

        return if (user == null) {
            logger.warn("User with ID $userId not found")
            ResponseEntity.notFound().build()
        } else {
            logger.info("User with ID $userId found and retrieved successfully")
            ResponseEntity.ok(user)
        }
    }

    @PostMapping("/add-or-update")
    fun addOrUpdateUser(@RequestBody user: AddOrUpdateUserRequest): ResponseEntity<String> {
        return try {
            logger.info("Received POST request to add/update user")
            userService.addOrUpdateUser(user)
            ResponseEntity(NO_CONTENT)
        } catch (ex: ValidationException) {
            logger.error("Validation error while adding/updating user: ${ex.message}")
            ResponseEntity.badRequest().body(ex.message)
        } catch (ex: HystrixRuntimeException) {
            logger.error("Hystrix runtime error while adding/updating user")
            ResponseEntity.internalServerError().body("Validation service is currently unavailable")
        } catch (ex: Exception) {
            logger.error("Internal server error while adding/updating user: ${ex.message}")
            ResponseEntity.internalServerError().body(ex.message)
        }
    }

    @DeleteMapping("/delete/{userId}")
    fun deleteUser(@PathVariable userId: String): ResponseEntity<Void> {
        logger.info("Received DELETE request to delete user with ID: $userId")

        return try {
            userService.deleteUser(userId)
            logger.info("User with ID $userId deleted successfully")
            ResponseEntity(NO_CONTENT)
        } catch (e: Exception) {
            logger.error("Internal server error while deleting user with ID $userId")
            ResponseEntity.internalServerError().build()
        }
    }
}