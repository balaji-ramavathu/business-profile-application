package com.profile.demo.profilevalidator.model

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument

@DynamoDBDocument
data class UserAddress(

    @DynamoDBAttribute
    var line1: String = "",

    @DynamoDBAttribute
    var line2: String? = null,

    @DynamoDBAttribute
    var city: String = "",

    @DynamoDBAttribute
    var zip: Long = 0,

    @DynamoDBAttribute
    var state: String = "",

    @DynamoDBAttribute
    var country: String = ""
)