package com.github.phoswald.sample.task;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class Task {

    private String taskId;
    private String userId;
    private String timestamp;
    private String title;
    private String description;
    private Boolean done;

    void setNewTaskId() {
        this.taskId = UUID.randomUUID().toString();
    }
    
    void validate() {
        requiresNonEmpty(taskId, "taskId");
        requiresNonEmpty(userId, "userId");
        requiresNonEmpty(timestamp, "timestamp");
    }

    private void requiresNonEmpty(String value, String name) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Field " + name + " must be set.");
        }
    }
}
