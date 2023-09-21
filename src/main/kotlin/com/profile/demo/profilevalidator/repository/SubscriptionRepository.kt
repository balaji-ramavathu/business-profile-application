package com.profile.demo.profilevalidator.repository

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.profile.demo.profilevalidator.model.Subscription
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Repository


@Repository
class SubscriptionRepository @Autowired constructor(@Lazy private val dynamoDBMapper: DynamoDBMapper) {

    fun addSubscription(subscription: Subscription) {
        dynamoDBMapper.save(subscription)
    }

    fun getSubscriptionsForUser(userId: String): List<Subscription> {
        val queryExpression = DynamoDBQueryExpression<Subscription>()
            .withKeyConditionExpression("userId = :userId")
            .withExpressionAttributeValues(mapOf(
                ":userId" to AttributeValue(userId)
            ))

        val results = dynamoDBMapper.query(Subscription::class.java, queryExpression)

        return results.filter { !it.deleted }
    }

    fun removeSubscription(subscription: Subscription) {
        if (dynamoDBMapper.load(subscription) != null) {
            dynamoDBMapper.save(subscription.copy(deleted = true))
        }
    }
}