package com.profile.demo.profilevalidator.config


import org.junit.jupiter.api.Assertions.*
import com.profile.demo.profilevalidator.client.ValidatorApiClient
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@Import(ExternalClientConfig::class)
class ExternalClientConfigTest {

    @Autowired
    private lateinit var validatorClient: ValidatorApiClient

    @Test
    fun testValidatorClientBean() {
         assertNotNull(validatorClient)
    }
}
