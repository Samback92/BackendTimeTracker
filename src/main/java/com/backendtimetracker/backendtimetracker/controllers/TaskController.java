package com.backendtimetracker.backendtimetracker.controllers;

import java.time.Duration;
import java.util.List;

import com.backendtimetracker.backendtimetracker.models.Task;
import com.backendtimetracker.backendtimetracker.services.TaskService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(origins= "http://localhost:5173")
public class TaskController {
    
    private TaskService taskService;


    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String getRoot() {
        System.out.println("Förfrågan mottagen");
        return "{'Hello World'}";
    }

    @GetMapping("/tasks")
    public List<Task> getAllTasks() {
        System.out.println("alla uppgifter");
        return taskService.getAllTasks();
    }

    @GetMapping("/tasks/{id}")
    public Task getTask(@PathVariable String id) {
        System.out.println("uppgift med id: " + id);
        return taskService.getTask(id);
    }
    
    @PostMapping("/tasks")
    public Task addTask(@RequestBody Task task) {
        System.out.println("Lägger till uppgift");
        return taskService.addTask(task);
    }

    @PatchMapping("/tasks/{id}")
    public Task updateTask(@PathVariable String id, @RequestBody Task task) {
        System.out.println("Uppdaterar uppgift med id: " + id);
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/tasks/{taskName}")
    public void deleteTask(@PathVariable String taskName) {
        System.out.println("Tar bort uppgift med namn: " + taskName);
        taskService.deleteTask(taskName);
    }

    @PostMapping("/tasks/{id}/start")
    public ResponseEntity<?> startTask(@PathVariable String id) {
        try {
            Task task = taskService.startTask(id);
            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Kunde inte starta uppgiften med id: " + id, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/tasks/{id}/end")
    public ResponseEntity<?> endTask(@PathVariable String id) {
        try {
            Task task = taskService.endTask(id);
            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Kunde inte avsluta uppgiften med id: " + id, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tasks/{taskName}/time")
    public ResponseEntity<?> getTotalTimeForTask(@PathVariable String taskName) {
        try {
            Duration  time = taskService.getTotalTimeForTask(taskName);
            String formattedTime = formatDuration(time);
            return new ResponseEntity<>(formattedTime, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Kunde inte hämta tid för uppgiften med namn: " + taskName, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String formatDuration(Duration duration) {
        long hours = duration.toHours();
        int minutes = duration.toMinutesPart();
        int seconds = duration.toSecondsPart();
        return String.format("%02d " + "hours: " + " %02d " + "minutes: " + " %02d " + "seconds:", hours, minutes, seconds);
    }

}



