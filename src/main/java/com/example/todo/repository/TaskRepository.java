package com.example.todo.repository;

import com.example.todo.model.Task;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TaskRepository {
    private final Map<String, Task> tasks = new HashMap<>();

    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    public Optional<Task> findById(String id) {
        return Optional.ofNullable(tasks.get(id));
    }

    public Task save(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

    public void delete(String id) {
        tasks.remove(id);
    }
}
