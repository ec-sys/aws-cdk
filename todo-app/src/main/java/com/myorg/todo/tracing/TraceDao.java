package com.myorg.todo.tracing;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TraceDao {

    private static final Logger LOG = LoggerFactory.getLogger(TraceDao.class);

    private final DynamoDbTemplate dynamoDbTemplate;

    private final DynamoDbClient dynamoDbClient;

    public TraceDao(DynamoDbTemplate dynamoDbTemplate, DynamoDbClient dynamoDbClient) {
        this.dynamoDbTemplate = dynamoDbTemplate;
        this.dynamoDbClient = dynamoDbClient;
    }

    @Async
    @EventListener(TracingEvent.class)
    public void storeTracingEvent(TracingEvent tracingEvent) {
        var test = dynamoDbClient.listTables();
        test.tableNames();
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
