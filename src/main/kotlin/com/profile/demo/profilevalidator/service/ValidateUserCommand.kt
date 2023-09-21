package com.profile.demo.profilevalidator.service
import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import com.profile.demo.profilevalidator.client.ValidatorApiClient
import com.profile.demo.profilevalidator.model.User

class ValidateUserCommand(
    private val validatorApiClient: ValidatorApiClient,
    private val productId: String,
    private val user: User
) : HystrixCommand<Boolean>(HystrixCommandGroupKey.Factory.asKey("ValidationGroup")) {

    override fun run(): Boolean {
        return validatorApiClient.validate(productId, user)
    }

    override fun getFallback(): Boolean {
        return false
    }
}
