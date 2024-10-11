package com.example.devsyncss.repository;

import com.example.devsyncss.entities.User;
import com.example.devsyncss.entities.enums.Role;
import com.example.devsyncss.repository.interfc.IUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;

import java.util.List;

public class UserRepository implements IUserRepository {

    private final EntityManager em;

    public UserRepository() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myJPAUnit");
        em = emf.createEntityManager();
    }

    public void save(User user) {
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
    }

    public User findById(Long id) {
        return em.find(User.class, id);
    }

    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    public User findByEmail(String email) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void update(User user) {
        em.getTransaction().begin();
        em.merge(user);
        em.getTransaction().commit();
    }

    public void delete(Long id) {
        User user = findById(id);
        if (user != null) {
            em.getTransaction().begin();
            em.remove(user);
            em.getTransaction().commit();
        }
    }

    public List<User> findAllUsersByRole(Role role) {
        return em.createQuery("SELECT u FROM User u WHERE u.role = :role", User.class)
                .setParameter("role", role)
                .getResultList();
    }

    public List<User> findAllUsersByManagerId(Long managerId) {
        return em.createQuery("SELECT u FROM User u WHERE u.manager.id = :managerId", User.class)
                .setParameter("managerId", managerId)
                .getResultList();
    }

    public Long findLatestUserId() {
        return em.createQuery("SELECT MAX(u.id) FROM User u", Long.class).getSingleResult();
    }
}