package com.profile.demo.profilevalidator.client

import com.profile.demo.profilevalidator.model.User
import com.profile.demo.profilevalidator.util.Constants.USERS_BASE_URL
import org.slf4j.LoggerFactory
import org.springframework.web.client.RestTemplate

class ValidatorApiClient {
    private val logger by lazy { LoggerFactory.getLogger(ValidatorApiClient::class.java) }

    fun validate(productId: String, user: User): Boolean {
        val restTemplate = RestTemplate()

        val response = restTemplate.postForEntity(
            "$USERS_BASE_URL/profile-validator/validate/$productId", user, String::class.java)

        return if (response.statusCode.is2xxSuccessful) {
            val responseData = response.body
            logger.info("Response: $responseData")
            responseData == "true"
        } else {
            logger.info("HTTP Request Failed with Status Code: ${response.statusCode}")
            false
        }
    }
}