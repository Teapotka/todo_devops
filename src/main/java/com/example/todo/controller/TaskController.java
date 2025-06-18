package com.example.todo.controller;

import com.example.todo.model.Task;
import com.example.todo.repository.TaskRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository repo;

    public TaskController(TaskRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return repo.findAll();
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        task.setId(UUID.randomUUID().toString());
        return repo.save(task);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable String id, @RequestBody Task updatedTask) {
        Task existing = repo.findById(id).orElseThrow();
        existing.setTitle(updatedTask.getTitle());
        existing.setCompleted(updatedTask.isCompleted());
        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable String id) {
        repo.delete(id);
    }
}
