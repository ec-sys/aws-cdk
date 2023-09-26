package com.myorg.todo.tracing;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TraceDao {

//    @Value("${spring.cloud.aws.credentials.access-key}")
//    private String accessKey;
//
//    @Value("${spring.cloud.aws.credentials.secret-key}")
//    private String secretKey;
//
//    @Value("${spring.cloud.aws.endpoint}")
//    private String endpoint;
//
//    @Value("${spring.cloud.aws.region.static}")
//    private Region region;

    private static final Logger LOG = LoggerFactory.getLogger(TraceDao.class);

    private final DynamoDbTemplate dynamoDbTemplate;

    private final DynamoDbClient dynamoDbClient;

    public TraceDao(DynamoDbTemplate dynamoDbTemplate, DynamoDbClient dynamoDbClient) {
        this.dynamoDbTemplate = dynamoDbTemplate;
        this.dynamoDbClient = dynamoDbClient;
    }

//    private CreateTableRequest getCreateTableRequest(String tableName, String key) {
//        CreateTableRequest request = CreateTableRequest.builder()
//                .attributeDefinitions(AttributeDefinition.builder()
//                        .attributeName(key)
//                        .attributeType(ScalarAttributeType.S)
//                        .build())
//                .keySchema(KeySchemaElement.builder()
//                        .attributeName(key)
//                        .keyType(KeyType.HASH)
//                        .build())
//                .provisionedThroughput(ProvisionedThroughput.builder()
//                        .readCapacityUnits(new Long(10))
//                        .writeCapacityUnits(new Long(10))
//                        .build())
//                .tableName(tableName)
//                .build();
//        return request;
//    }

    @Async
    @EventListener(TracingEvent.class)
    public void storeTracingEvent(TracingEvent tracingEvent) {
//        var client = DynamoDbClient.builder()
//                .endpointOverride(URI.create(endpoint))
//                .region(region)
//                .credentialsProvider(StaticCredentialsProvider.create(
//                        AwsBasicCredentials.create(accessKey, secretKey)))
//                .build();
//        var responseCrateTbl = client.createTable(getCreateTableRequest("test_tbl_1", "id"));
//        var test = client.listTables();
//        test.tableNames();
        DescribeTableRequest request = DescribeTableRequest.builder().tableName("todo_breadcrumbs").build();
        var response = dynamoDbClient.describeTable(request);
        response.table().hasReplicas();


        TodoBreadcrumb breadcrumb = new TodoBreadcrumb();
        breadcrumb.setId(UUID.randomUUID().toString());
        breadcrumb.setUri(tracingEvent.getUri());
        breadcrumb.setUsername(tracingEvent.getUsername());
        breadcrumb.setTimestamp(ZonedDateTime.now().toString());

        dynamoDbTemplate.save(breadcrumb);

        LOG.info("Successfully stored breadcrumb trace");
    }

    public List<TodoBreadcrumb> findAllEventsForUser(String username) {
        TodoBreadcrumb breadcrumb = new TodoBreadcrumb();
        breadcrumb.setUsername(username);

        return dynamoDbTemplate.query(
                QueryEnhancedRequest
                        .builder()
                        .queryConditional(
                                QueryConditional.keyEqualTo(
                                        Key
                                                .builder()
                                                .partitionValue(breadcrumb.getId())
                                                .build()
                                )
                        )
                        .build(),
                TodoBreadcrumb.class
        ).items()
                .stream()
                .collect(Collectors.toList());
    }

    public List<TodoBreadcrumb> findUserTraceForLastTwoWeeks(String username) {
        ZonedDateTime twoWeeksAgo = ZonedDateTime.now().minusWeeks(2);

        TodoBreadcrumb breadcrumb = new TodoBreadcrumb();
        breadcrumb.setUsername(username);

        return dynamoDbTemplate.query(
                QueryEnhancedRequest
                        .builder()
                        .queryConditional(
                                QueryConditional.keyEqualTo(
                                        Key
                                                .builder()
                                                .partitionValue(breadcrumb.getId())
                                                .build()
                                )
                        )
                        .filterExpression(
                                Expression
                                        .builder()
                                        .expression("timestamp > :twoWeeksAgo")
                                        .putExpressionValue(":twoWeeksAgo",
                                                AttributeValue
                                                        .builder()
                                                        .s(twoWeeksAgo.toString())
                                                        .build()
                                        )
                                        .build()
                        )
                        .build(),
                TodoBreadcrumb.class
        )
                .items()
                .stream()
                .collect(Collectors.toList());
    }
}
