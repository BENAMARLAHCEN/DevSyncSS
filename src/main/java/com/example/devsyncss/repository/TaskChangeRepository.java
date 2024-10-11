package com.example.devsyncss.repository;

import com.example.devsyncss.entities.TaskChange;
import com.example.devsyncss.entities.User;
import com.example.devsyncss.repository.interfc.ITaskChangeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class TaskChangeRepository implements ITaskChangeRepository {
    private final EntityManager em;

    public TaskChangeRepository() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myJPAUnit");
        em = emf.createEntityManager();
    }

    @Override
    public void createTaskChange(TaskChange taskChange) {
        em.getTransaction().begin();
        em.persist(taskChange);
        em.getTransaction().commit();
    }

    @Override
    public void deleteTaskChange(Long id) {
        TaskChange taskChange = em.find(TaskChange.class, id);
        if (taskChange != null) {
            em.getTransaction().begin();
            em.remove(taskChange);
            em.getTransaction().commit();
        }
    }

    @Override
    public TaskChange getTaskChange(Long id) {
        return em.find(TaskChange.class, id);
    }

    @Override
    public void updateTaskChange(TaskChange taskChange) {
        em.getTransaction().begin();
        em.merge(taskChange);
        em.getTransaction().commit();
    }

    @Override
    public List<TaskChange> getAllManagerCreateTaskChanges(Long managerId) {
        return em.createQuery("SELECT tc FROM TaskChange tc WHERE tc.task.createdBy.id = :managerId", TaskChange.class)
                .setParameter("managerId", managerId)
                .getResultList();
    }

    @Override
    public List<TaskChange> getUserChangeRequests(User user) {
        return em.createQuery("SELECT tc FROM TaskChange tc WHERE tc.user.id = :userId AND tc.changeType = 'REQUEST'", TaskChange.class)
                .setParameter("userId", user.getId())
                .getResultList();
    }

}
