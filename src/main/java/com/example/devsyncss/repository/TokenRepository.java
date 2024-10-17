package com.example.devsyncss.repository;

import com.example.devsyncss.entities.Token;
import com.example.devsyncss.entities.User;
import com.example.devsyncss.repository.interfc.ITokenRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class TokenRepository implements ITokenRepository {
    private final EntityManager em;

    public TokenRepository() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myJPAUnit");
        em = emf.createEntityManager();
    }

    public void createToken(Token token) {
        em.getTransaction().begin();
        em.persist(token);
        em.getTransaction().commit();
    }

    public Token getToken(Long id) {
        return em.find(Token.class, id);
    }

    public void updateToken(Token token) {
        em.getTransaction().begin();
        em.merge(token);
        em.getTransaction().commit();
    }

    public boolean deleteToken(Long id) {
        Token token = em.find(Token.class, id);
        if (token == null) {
            return false;
        }
        em.getTransaction().begin();
        em.remove(token);
        em.getTransaction().commit();
        return true;
    }

    public void deleteAllTokensPassResetDate() {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Token t WHERE t.lastResetDate < CURRENT_DATE").executeUpdate();
        em.getTransaction().commit();
    }

    public Token getUserTokens(User user) {
        return em.createQuery("SELECT t FROM Token t WHERE t.user = :user", Token.class)
                .setParameter("user", user)
                .getSingleResult();
    }

    public List<Token> getAllTokens() {
        return em.createQuery("SELECT t FROM Token t", Token.class).getResultList();
    }

    public void save(Token token) {
        em.getTransaction().begin();
        em.persist(token);
        em.getTransaction().commit();
    }

    public Token getTokenByUserId(Long userId) {
        return em.createQuery("SELECT t FROM Token t WHERE t.user.id = :userId", Token.class)
                .setParameter("userId", userId)
                .getSingleResult();
    }
}
