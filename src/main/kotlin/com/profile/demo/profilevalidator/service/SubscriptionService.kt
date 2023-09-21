package com.profile.demo.profilevalidator.service

import com.profile.demo.profilevalidator.model.Subscription
import com.profile.demo.profilevalidator.repository.SubscriptionRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SubscriptionService @Autowired constructor(
    private val subscriptionRepository: SubscriptionRepository) {

    private val logger by lazy { LoggerFactory.getLogger(SubscriptionService::class.java) }

    fun addSubscriptions(userId: String, productIds: List<String>) {
        logger.info("Adding subscriptions for user ID: $userId")

        productIds.forEach {
            subscriptionRepository.addSubscription(Subscription(userId, it))
        }
    }

    fun getSubscribedProducts(userId: String): List<String> {
        logger.info("Fetching user with ID: $userId")

        val subscriptions = try {
            subscriptionRepository.getSubscriptionsForUser(userId)
        } catch (e: UnsupportedOperationException) {
            logger.warn("No subscriptions found for user with ID: $userId")
            throw NoSuchElementException("No subscriptions found for user with ID $userId")
        }

        return subscriptions.map { it.productId }
    }

    fun removeSubscription(userId: String, productId: String) {
        logger.info("Removing subscription for user ID: $userId for product : $productId")
        subscriptionRepository.removeSubscription(Subscription(userId, productId))
        logger.info("Removed subscription for user ID: $userId for product : $productId")
    }

}