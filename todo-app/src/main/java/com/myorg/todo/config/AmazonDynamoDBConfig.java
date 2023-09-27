package com.myorg.todo.config;

import io.awspring.cloud.dynamodb.DefaultDynamoDbTableSchemaResolver;
import io.awspring.cloud.dynamodb.DynamoDbTableNameResolver;
import io.awspring.cloud.dynamodb.DynamoDbTableSchemaResolver;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import jakarta.persistence.Version;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;
import java.util.Locale;

@Configuration
@Slf4j
public class AmazonDynamoDBConfig {

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${spring.cloud.aws.endpoint}")
    private String endpoint;

    @Value("${spring.cloud.aws.region.static}")
    private Region region;

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .endpointOverride(URI.create(endpoint))
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient() {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient())
                .build();
    }

    @Bean
    public DynamoDbTemplate dynamoDbTemplate() {
        DynamoDbTableNameResolver nameResolver = dynamoDbTableNameResolver();
        DynamoDbTableSchemaResolver schemaResolver = dynamoDbTableSchemaResolver();
        return new DynamoDbTemplate(dynamoDbEnhancedClient(), schemaResolver, nameResolver);
        // return new DynamoDbTemplate(dynamoDbEnhancedClient());
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