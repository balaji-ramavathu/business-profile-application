package com.profile.demo.profilevalidator.validators

import com.profile.demo.profilevalidator.model.User

object TSheets: ProfileValidator {
    override fun validate(productId: String, user: User): Boolean {
        // Note:: custom validation logic need to be implemented here
        // For the sake of demo, bootstrapping default logic of email cannot be blank
        return !user.email.isNullOrEmpty()
    }
}