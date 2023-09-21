package com.profile.demo.profilevalidator.controller

import com.profile.demo.profilevalidator.model.Subscription
import com.profile.demo.profilevalidator.service.SubscriptionService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class SubscriptionControllerTest {

    @Mock
    private lateinit var subscriptionService: SubscriptionService

    @InjectMocks
    private lateinit var subscriptionController: SubscriptionController

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testAddSubscription() {
        val subscription = Subscription(userId = "user1", productId = "product1")

        subscriptionController.addSubscription(subscription)

        Mockito.verify(subscriptionService).addSubscriptions(subscription.userId, listOf(subscription.productId))
    }

    @Test
    fun testGetSubscriptionsForUser_Success() {
        val userId = "user1"
        val productIds = listOf("product1", "product2")

        Mockito.`when`(subscriptionService.getSubscribedProducts(userId)).thenReturn(productIds)

        val response: ResponseEntity<List<String>> = subscriptionController.getSubscriptionsForUser(userId)

        assertNotNull(response)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(productIds, response.body)
    }

    @Test
    fun testGetSubscriptionsForUser_NotFound() {
        val userId = "user2"

        Mockito.`when`(subscriptionService.getSubscribedProducts(userId)).thenThrow(NoSuchElementException())

        val response: ResponseEntity<List<String>> = subscriptionController.getSubscriptionsForUser(userId)

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
    }

    @Test
    fun testRemoveSubscription() {
        val subscription = Subscription(userId = "user1", productId = "product1")

        subscriptionController.removeSubscription(subscription)

        Mockito.verify(subscriptionService).removeSubscription(subscription.userId, subscription.productId)
    }
}
