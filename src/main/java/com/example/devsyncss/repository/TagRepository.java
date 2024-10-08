package com.example.devsyncss.repository;

import com.example.devsyncss.entities.Tag;
import com.example.devsyncss.repository.interfc.ITagRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Optional;

public class TagRepository implements ITagRepository {
    private final EntityManager em;

    public TagRepository() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myJPAUnit");
        em = emf.createEntityManager();
    }

    @Override
    public void createTag(Tag tag) {
        em.getTransaction().begin();
        em.persist(tag);
        em.getTransaction().commit();
    }

    @Override
    public void deleteTag(Long id) {
        Tag tag = em.find(Tag.class, id);
        if (tag != null) {
            em.getTransaction().begin();
            em.remove(tag);
            em.getTransaction().commit();
        }
    }

    @Override
    public List<Tag> getAllTags() {
        return em.createQuery("SELECT t FROM Tag t", Tag.class).getResultList();
    }

    @Override
    public Tag getTag(Long id) {
        return em.find(Tag.class, id);
    }

    @Override
    public Optional<Tag> getTag(String name) {
        return em.createQuery("SELECT t FROM Tag t WHERE t.name = :name", Tag.class)
                .setParameter("name", name)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public void updateTag(Tag tag) {
        em.getTransaction().begin();
        em.merge(tag);
        em.getTransaction().commit();
    }
}
