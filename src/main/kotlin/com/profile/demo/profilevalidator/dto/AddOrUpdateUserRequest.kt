package com.profile.demo.profilevalidator.dto

import com.profile.demo.profilevalidator.model.UserAddress

data class AddOrUpdateUserRequest(
    val id: String? = null,
    val legalName: String,
    val companyName: String,
    val businessAddress: UserAddress,
    val legalAddress: UserAddress,
    val taxId: String,
    val email: String,
    val website: String,
)