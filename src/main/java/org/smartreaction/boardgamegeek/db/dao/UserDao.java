package org.smartreaction.boardgamegeek.db.dao;

import org.smartreaction.boardgamegeek.db.entities.User;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;

@Stateless
public class UserDao
{
    @PersistenceContext
    EntityManager em;

    public List<User> getUsers()
    {
        TypedQuery<User> query = em.createQuery("select u from User u", User.class);
        return query.getResultList();
    }

    public User getUser(String username)
    {
        try {
            TypedQuery<User> query = em.createQuery("select u from User u where u.username = :username", User.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public void createUser(User user)
    {
        em.persist(user);
    }

    public void saveUser(User user)
    {
        em.merge(user);
    }

    public void flush()
    {
        em.flush();
    }

    public List<User> getRecentlyLoggedInUsers()
    {
        TypedQuery<User> query = em.createQuery("select u from User u order by u.lastLogin desc", User.class);
        query.setMaxResults(20);

        return query.getResultList();
    }
}
