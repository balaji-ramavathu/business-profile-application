package com.profile.demo.profilevalidator.service

import com.profile.demo.profilevalidator.model.Subscription
import com.profile.demo.profilevalidator.repository.SubscriptionRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*

class SubscriptionServiceTest {

    private val subscriptionRepository: SubscriptionRepository = mock(SubscriptionRepository::class.java)
    private val subscriptionService = SubscriptionService(subscriptionRepository)

    @Test
    fun testAddSubscriptions() {
        
        val userId = "123"
        val productIds = listOf("product1", "product2", "product3")
        
        subscriptionService.addSubscriptions(userId, productIds)

        verify(subscriptionRepository).addSubscription(Subscription(userId, "product1"))
        verify(subscriptionRepository).addSubscription(Subscription(userId, "product2"))
        verify(subscriptionRepository).addSubscription(Subscription(userId, "product3"))
    }

    @Test
    fun testGetSubscribedProducts() {
        
        val userId = "456"
        val subscriptions = listOf(
            Subscription(userId, "productA"),
            Subscription(userId, "productB")
        )
        `when`(subscriptionRepository.getSubscriptionsForUser(userId)).thenReturn(subscriptions)

        val subscribedProducts = subscriptionService.getSubscribedProducts(userId)

        verify(subscriptionRepository).getSubscriptionsForUser(userId)
        assert(subscribedProducts.contains("productA"))
        assert(subscribedProducts.contains("productB"))
        assert(subscribedProducts.size == 2)
    }

    @Test
    fun testGetSubscribedProducts_NoSubscriptions() {
        
        val userId = "789"
        `when`(subscriptionRepository.getSubscriptionsForUser(userId)).thenReturn(emptyList())

        val subscribedProducts = subscriptionService.getSubscribedProducts(userId)

        verify(subscriptionRepository).getSubscriptionsForUser(userId)
        assert(subscribedProducts.isEmpty())
    }

    @Test
    fun testGetSubscribedProducts_Exception() {
        
        val userId = "999"
        `when`(subscriptionRepository.getSubscriptionsForUser(userId))
            .thenThrow(UnsupportedOperationException())

        assertThrows<NoSuchElementException> {
            subscriptionService.getSubscribedProducts(userId)
        }
    }

    @Test
    fun testRemoveSubscription() {
        val userId = "123"
        val productId = "product1"

        subscriptionService.removeSubscription(userId, productId)

        verify(subscriptionRepository).removeSubscription(Subscription(userId, productId))
    }
}
