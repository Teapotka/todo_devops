package com.example.todo.repository;

import com.example.todo.model.Task;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

@Repository
public class TaskRepository {

    private final DynamoDbClient dynamoDbClient;
    private final String tableName = "tasks";

    public TaskRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public List<Task> findAll() {
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(tableName)
                .build();

        ScanResponse response = dynamoDbClient.scan(scanRequest);

        List<Task> tasks = new ArrayList<>();
        for (Map<String, AttributeValue> item : response.items()) {
            Task task = new Task();
            task.setId(item.get("id").s());
            task.setTitle(item.get("title").s());
            task.setCompleted(item.get("completed").bool());
            tasks.add(task);
        }
        return tasks;
    }

    public Optional<Task> findById(String id) {
        Map<String, AttributeValue> key = Map.of(
                "id", AttributeValue.builder().s(id).build()
        );

        GetItemResponse response = dynamoDbClient.getItem(GetItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build());

        if (response.hasItem()) {
            Map<String, AttributeValue> item = response.item();
            Task task = new Task();
            task.setId(item.get("id").s());
            task.setTitle(item.get("title").s());
            task.setCompleted(item.get("completed").bool());
            return Optional.of(task);
        } else {
            return Optional.empty();
        }
    }

    public Task save(Task task) {
        Map<String, AttributeValue> item = Map.of(
                "id", AttributeValue.builder().s(task.getId()).build(),
                "title", AttributeValue.builder().s(task.getTitle()).build(),
                "completed", AttributeValue.builder().bool(task.isCompleted()).build()
        );

        dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build());

        return task;
    }

    public void delete(String id) {
        Map<String, AttributeValue> key = Map.of(
                "id", AttributeValue.builder().s(id).build()
        );

        dynamoDbClient.deleteItem(DeleteItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build());
    }
}
