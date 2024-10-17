package com.example.devsyncss.repository;

import com.example.devsyncss.entities.Task;
import com.example.devsyncss.entities.User;
import com.example.devsyncss.repository.interfc.ITaskRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class TaskRepository implements ITaskRepository {
    public final EntityManager em;

    public TaskRepository() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myJPAUnit");
        em = emf.createEntityManager();
    }

    public Task createTask(Task task) {
        em.getTransaction().begin();
        em.persist(task);
        em.getTransaction().commit();
        return task;
    }

    public List<Task> getAllTasks() {
       // return em.createQuery("SELECT t FROM Task t", Task.class).getResultList();
        List<Task> taskList =em.createQuery("SELECT t FROM Task t", Task.class).getResultList();
        taskList.forEach(task -> {
            em.refresh(task);
        });
        return taskList;
    }

    public List<Task> findOverdueTasks() {
        return em.createQuery("SELECT t FROM Task t WHERE t.dueDate < CURRENT_DATE", Task.class).getResultList();
    }

    public List<Task> findTasksDueSoon(java.time.LocalDateTime endDate) {
        return em.createQuery("SELECT t FROM Task t WHERE t.dueDate BETWEEN CURRENT_DATE AND :endDate", Task.class)
                .setParameter("endDate", endDate)
                .getResultList();
    }



    public List<Task> searchTasks(String searchTerm) {
        return em.createQuery("SELECT t FROM Task t WHERE t.title LIKE :searchTerm OR t.description LIKE :searchTerm", Task.class)
                .setParameter("searchTerm", "%" + searchTerm + "%")
                .getResultList();
    }

    public Double getTaskCompletionRate(User user, java.time.LocalDateTime startDate, java.time.LocalDateTime endDate) {
        Long totalTasks = em.createQuery("SELECT COUNT(t) FROM Task t WHERE t.assignedTo = :user AND t.creationDate BETWEEN :startDate AND :endDate", Long.class)
                .setParameter("user", user)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getSingleResult();
        Long completedTasks = em.createQuery("SELECT COUNT(t) FROM Task t WHERE t.assignedTo = :user AND t.creationDate BETWEEN :startDate AND :endDate AND t.status = 'COMPLETED'", Long.class)
                .setParameter("user", user)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getSingleResult();
        return (double) completedTasks / totalTasks;
    }

    public List<Task> findStagnantTasks(java.time.LocalDateTime stagnantDate) {
        return em.createQuery("SELECT t FROM Task t WHERE t.status = 'IN_PROGRESS' AND t.dueDate < :stagnantDate", Task.class)
                .setParameter("stagnantDate", stagnantDate)
                .getResultList();
    }

    public Task getTask(Long id) {
        return em.find(Task.class, id);
    }

    public void updateTask(Task task) {
        em.getTransaction().begin();
        em.merge(task);
        em.getTransaction().commit();
    }


    public void deleteTask(Long id) {
        Task task = em.find(Task.class, id);
        if (task != null) {
            em.getTransaction().begin();
            em.remove(task);
            em.getTransaction().commit();
        }
    }

    public List<Task> getUserCreatedTasks(User user) {
        return em.createQuery("SELECT t FROM Task t WHERE t.createdBy = :user", Task.class)
                .setParameter("user", user)
                .getResultList();
    }
}
