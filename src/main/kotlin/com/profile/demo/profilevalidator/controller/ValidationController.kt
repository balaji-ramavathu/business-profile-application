package com.profile.demo.profilevalidator.controller

import com.profile.demo.profilevalidator.model.User
import com.profile.demo.profilevalidator.validators.ProfileValidatorProvider
import org.springframework.web.bind.annotation.*


@RestController
@CrossOrigin(origins = ["http://localhost:3000"])
@RequestMapping("/profile-validator")
class ValidationController {

    @PostMapping("/validate/{productId}")
    fun validate(@PathVariable productId: String, @RequestBody user: User): Boolean {
        return ProfileValidatorProvider.getValidator(productId).validate(productId, user)
    }
}