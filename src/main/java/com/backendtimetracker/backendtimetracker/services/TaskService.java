package com.backendtimetracker.backendtimetracker.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.backendtimetracker.backendtimetracker.models.Task;

@Service
public class TaskService {
    private final MongoOperations mongoOperations;
    
    public TaskService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public List<Task> getAllTasks() {
        return mongoOperations.findAll(Task.class);
    }

    public Task getTask(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        return mongoOperations.findOne(query, Task.class);
    }

    public Task addTask(Task task) {
        task.setActive(true);
        task.setStartTime(LocalDateTime.now());
        return mongoOperations.insert(task);
    }

    public Task updateTask(String id, Task task) {
        Query query = Query.query(Criteria.where("id").is(id));
        Update update = new Update();
    
        // Hämta den befintliga uppgiften för att jämföra värden
        Task existingTask = mongoOperations.findById(id, Task.class);
    
        if (task.getTaskName() != null && !task.getTaskName().equals(existingTask.getTaskName())) {
            // Om taskName ändras, uppdatera det och behåll det befintliga active-värdet
            update.set("taskName", task.getTaskName());
            update.set("active", existingTask.isActive());
        } else if (task.isActive()!= existingTask.isActive()) {
            // Om active ändras, uppdatera det och sätt endTime om det ändras till false
            update.set("active", task.isActive());
            if (!task.isActive()) {
                update.set("endTime", LocalDateTime.now());
            }
        }
    
        mongoOperations.updateFirst(query, update, Task.class);
        return mongoOperations.findById(id, Task.class);
    }
    
    
    public Task startTask(String id) {
        // Hämta den befintliga uppgiften
        Task existingTask = mongoOperations.findById(id, Task.class);

        // Skapa en ny uppgift med samma namn men ett nytt ID
        Task newTask = new Task();
        newTask.setTaskName(existingTask.getTaskName());
        newTask.setActive(true);
        newTask.setStartTime(LocalDateTime.now());

        // Spara den nya uppgiften i databasen
        mongoOperations.save(newTask);

        return newTask;
    }

    public Task endTask(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("active", false);
        update.set("endTime", LocalDateTime.now());
        mongoOperations.updateFirst(query, update, Task.class);
        return mongoOperations.findAndModify(query, update, Task.class);
    }
    

    public void deleteTask(String taskName) {
        Query query = Query.query(Criteria.where("taskName").is(taskName));
        mongoOperations.remove(query, Task.class);
    }

    public Duration getTotalTimeForTask(String taskName) {
        // Hämta alla uppgifter med samma namn
        List<Task> tasks = mongoOperations.find(Query.query(Criteria.where("taskName").is(taskName)), Task.class);
    
        // Summera tiden för alla uppgifter
        Duration totalDuration = Duration.ZERO;
        for (Task task : tasks) {
            if (task.getEndTime() != null) {
                Duration duration = Duration.between(task.getStartTime(), task.getEndTime());
                totalDuration = totalDuration.plus(duration);
            }
        }
    
        return totalDuration;
    }

}
