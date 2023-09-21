package com.profile.demo.profilevalidator.controller

import com.profile.demo.profilevalidator.model.Subscription
import com.profile.demo.profilevalidator.service.SubscriptionService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@CrossOrigin(origins = ["http://localhost:3000"])
@RequestMapping("/subscription")
class SubscriptionController @Autowired constructor(val subscriptionService: SubscriptionService) {
    private val logger by lazy { LoggerFactory.getLogger(SubscriptionController::class.java) }

    @PostMapping("/add")
    fun addSubscription(@RequestBody subscription: Subscription) {
        logger.info("Received POST request to add subscription")
        subscriptionService.addSubscriptions(
            subscription.userId,
            listOf(subscription.productId))
    }

    @GetMapping("/get/{userId}")
    fun getSubscriptionsForUser(@PathVariable userId: String): ResponseEntity<List<String>> {
        logger.info("Received GET request to retrieve subscriptions for user ID: $userId")
        return try {
            val subscriptions = subscriptionService.getSubscribedProducts(userId)
            logger.info("Subscription for User ID $userId found and retrieved successfully")
            ResponseEntity.ok(subscriptions)
        } catch (e: NoSuchElementException) {
            logger.warn("No subscriptions found for user with ID $userId")
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/remove")
    fun removeSubscription(@RequestBody subscription: Subscription) {
        logger.info("Received POST request to remove subscription")
        subscriptionService.removeSubscription(
            subscription.userId,
            subscription.productId)
    }
}