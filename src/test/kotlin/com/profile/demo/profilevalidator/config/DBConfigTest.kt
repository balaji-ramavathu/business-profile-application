package com.profile.demo.profilevalidator.config

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest
@ActiveProfiles("test")
@Import(DBConfig::class)
class DBConfigTest {

    @Autowired
    private lateinit var dbConfig: DBConfig

    @Test
    fun testDynamoDBMapperBean() {
        val dynamoDBMapper = dbConfig.dynamoDBMapper()
         assertNotNull(dynamoDBMapper)
    }
}