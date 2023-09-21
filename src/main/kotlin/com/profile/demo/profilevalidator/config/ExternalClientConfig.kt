package com.profile.demo.profilevalidator.config

import com.profile.demo.profilevalidator.client.ValidatorApiClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Lazy
@Configuration
class ExternalClientConfig {

    @Bean
    fun validatorClient(): ValidatorApiClient {
        return ValidatorApiClient()
    }
}