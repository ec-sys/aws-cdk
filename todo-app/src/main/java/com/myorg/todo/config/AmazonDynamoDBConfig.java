package com.myorg.todo.config;

import io.awspring.cloud.dynamodb.DefaultDynamoDbTableSchemaResolver;
import io.awspring.cloud.dynamodb.DynamoDbTableNameResolver;
import io.awspring.cloud.dynamodb.DynamoDbTableSchemaResolver;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
@Slf4j
public class AmazonDynamoDBConfig {

    @Autowired
    private DynamoDbClient dynamoDbClient;

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient() {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Bean
    public DynamoDbTemplate dynamoDbTemplate() {
        DynamoDbTableNameResolver nameResolver = dynamoDbTableNameResolver();
        DynamoDbTableSchemaResolver schemaResolver = dynamoDbTableSchemaResolver();
        return new DynamoDbTemplate(dynamoDbEnhancedClient(), schemaResolver, nameResolver);
    }

    @Bean
    public DynamoDbTableNameResolver dynamoDbTableNameResolver() {
        return new DynamoDbTableNameResolver() {
            @Override
            public <T> String resolve(Class<T> clazz) {
                return clazz.getAnnotation(DynamoEntity.class).table();
            }
        };
    }

    @Bean
    public DynamoDbTableSchemaResolver dynamoDbTableSchemaResolver() {
        return new DefaultDynamoDbTableSchemaResolver();
    }
}