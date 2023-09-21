package com.profile.demo.profilevalidator.model

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable

@DynamoDBTable(tableName = "subscriptions")
data class Subscription(

    @DynamoDBHashKey
    @DynamoDBAttribute
    var userId: String = "",

    @DynamoDBRangeKey
    @DynamoDBAttribute
    var productId: String = "",

    @DynamoDBAttribute
    var deleted: Boolean = false
)