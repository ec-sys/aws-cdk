package com.myorg.todo.config;

import com.myorg.todo.tracing.TodoBreadcrumb;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

@Component
@Slf4j
public class AppStartEventHandler {

    private final static long READ_CAPACITY_UNITS = 5;
    private final static long WRITE_CAPACITY_UNITS = 5;

    private final DynamoDbTemplate dynamoDbTemplate;
    private final DynamoDbClient dynamoDbClient;

    public AppStartEventHandler(DynamoDbTemplate dynamoDbTemplate, DynamoDbClient dynamoDbClient) {
        this.dynamoDbTemplate = dynamoDbTemplate;
        this.dynamoDbClient = dynamoDbClient;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doCreateDynamoTable() {
        createTodoBreadcrumbTable();
    }

    private void createTodoBreadcrumbTable() {
        String tableName = TodoBreadcrumb.class.getAnnotation(DynamoEntity.class).table();

        DescribeTableRequest describeRequest = DescribeTableRequest.builder().tableName(tableName).build();
        try {
            dynamoDbClient.describeTable(describeRequest);
        } catch (ResourceNotFoundException ex) {
            log.info("table {} is not exist then create new", tableName);
            AttributeDefinition idAtt = AttributeDefinition.builder()
                    .attributeName("id")
                    .attributeType(ScalarAttributeType.S)
                    .build();
            KeySchemaElement idSchema = KeySchemaElement.builder()
                    .attributeName("id")
                    .keyType(KeyType.HASH)
                    .build();
            AttributeDefinition timestampAtt = AttributeDefinition.builder()
                    .attributeName("timestamp")
                    .attributeType(ScalarAttributeType.S)
                    .build();
            KeySchemaElement timestampSchema = KeySchemaElement.builder()
                    .attributeName("timestamp")
                    .keyType(KeyType.RANGE)
                    .build();

            CreateTableRequest request = CreateTableRequest.builder()
                    .attributeDefinitions(idAtt, timestampAtt)
                    .keySchema(idSchema, timestampSchema)
                    .provisionedThroughput(ProvisionedThroughput.builder()
                            .readCapacityUnits(READ_CAPACITY_UNITS)
                            .writeCapacityUnits(WRITE_CAPACITY_UNITS)
                            .build())
                    .tableName(tableName)
                    .build();
            dynamoDbClient.createTable(request);
        }
    }
}
