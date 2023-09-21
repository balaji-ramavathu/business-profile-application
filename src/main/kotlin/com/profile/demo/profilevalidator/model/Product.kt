package com.profile.demo.profilevalidator.model

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable

@DynamoDBTable(tableName = "products")
data class Product(

    @DynamoDBHashKey
    var id: String = "",

    @DynamoDBAttribute
    var name: String = "",

    @DynamoDBAttribute
    var type: String = "",

    @DynamoDBAttribute
    var active: Boolean = true,

    @DynamoDBAttribute
    var addedAt: Long = 0,

    @DynamoDBAttribute
    var updatedAt: Long = 0
)
