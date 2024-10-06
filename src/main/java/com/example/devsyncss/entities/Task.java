package com.example.devsyncss.entities;

import com.example.devsyncss.entities.enums.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(name = "creation_date", nullable = false)
    @PastOrPresent
    private LocalDateTime creationDate;

    @Column(name = "due_date", nullable = false)
    @Future
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status;

    @ManyToMany
    @JoinTable(
            name = "task_tags",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Size(min = 2, message = "At least two tags are required")
    private Set<Tag> tags;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "is_locked")
    private boolean isLocked;

    public Task() {
    }

    public Task(String title, String description, LocalDateTime creationDate, LocalDateTime dueDate, TaskStatus status, Set<Tag> tags, User assignedTo, User createdBy, boolean isLocked) {
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.dueDate = dueDate;
        this.status = status;
        this.tags = tags;
        this.assignedTo = assignedTo;
        this.createdBy = createdBy;
        this.isLocked = isLocked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public @PastOrPresent LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(@PastOrPresent LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public @Future LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(@Future LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public @Size(min = 2, message = "At least two tags are required") Set<Tag> getTags() {
        return tags;
    }

    public void setTags(@Size(min = 2, message = "At least two tags are required") Set<Tag> tags) {
        this.tags = tags;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }
}