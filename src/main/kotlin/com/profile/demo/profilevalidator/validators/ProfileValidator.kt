package com.profile.demo.profilevalidator.validators

import com.profile.demo.profilevalidator.model.User

interface ProfileValidator {
    fun validate(productId: String, user: User): Boolean
}