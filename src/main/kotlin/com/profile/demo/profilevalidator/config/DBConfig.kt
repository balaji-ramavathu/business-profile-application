package com.profile.demo.profilevalidator.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Lazy
@Configuration
class DBConfig {

    @Value("\${aws.accessKey}")
    private lateinit var awsAccessKey: String

    @Value("\${aws.secretKey}")
    private lateinit var awsSecretKey: String

    @Value("\${aws.region}")
    private lateinit var awsRegion: String



    private fun buildDynamoDB(): AmazonDynamoDB {
        val credentials = BasicAWSCredentials(awsAccessKey, awsSecretKey)

        return AmazonDynamoDBClientBuilder
            .standard()
            .withRegion(awsRegion)
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .build()

    }

    @Bean
    fun dynamoDBMapper(): DynamoDBMapper {
        return DynamoDBMapper(buildDynamoDB())
    }

}