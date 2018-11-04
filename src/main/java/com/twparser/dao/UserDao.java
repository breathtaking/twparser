package com.twparser.dao;

import com.twparser.model.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.List;

/**
 * Performs DB interaction for User entity
 */

public class UserDao {
    public User getUser (String userId) { return null; }
    public List<User> getAllUsers() { return null;}

    boolean isUserExistsInDataBase(String userId) {
        Session session = null;
        try {
            session = HibernateSessionFactory.getUserSessionFactory().openSession();
            session.isOpen();
            session.getTransaction().begin();
            String hql = "SELECT u.profileIdentifier FROM User u";
            Query query = session.createQuery(hql);
            List objectList = query.list();
            if (objectList.contains(userId)) return true;
            System.out.println("user existing check: " + userId + ": " + objectList.contains(userId));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Some error occurred during checking user: " + userId);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
                System.out.println("Session is closed");
            }
        }
        return false;
    }

    public boolean deleteUser (String userId) { return true;}

    boolean addUser (User user) throws SQLException {
        Session session = null;
        try {
            session = HibernateSessionFactory.getUserSessionFactory().openSession();
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return true;
    }
    public boolean refreshStatus (String userId) { return true; }

}