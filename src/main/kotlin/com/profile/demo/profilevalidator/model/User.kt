package com.profile.demo.profilevalidator.model

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable

@DynamoDBTable(tableName = "users")
data class User (

    @DynamoDBHashKey
    var id: String = "",

    @DynamoDBAttribute
    var legalName: String = "",

    @DynamoDBAttribute
    var companyName: String = "",

    @DynamoDBAttribute
    var businessAddress: UserAddress = UserAddress(),

    @DynamoDBAttribute
    var legalAddress: UserAddress = UserAddress(),

    @DynamoDBAttribute
    var taxId: String = "",

    @DynamoDBAttribute
    var email: String? = "",

    @DynamoDBAttribute
    var website: String? = "",

    @DynamoDBAttribute
    var addedAt: Long = 0,

    @DynamoDBAttribute
    var updatedAt: Long = 0,

    @DynamoDBAttribute
    var active: Boolean = false
)
