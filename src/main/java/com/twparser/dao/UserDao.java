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
    public boolean checkUser (String userId) {
        Session session = null;
        try {
            session = HibernateSessionFactory.getUserSessionFactory().openSession();
            session.isOpen();
            session.getTransaction().begin();
            String hql = "SELECT u.userId FROM User u";
            Query query = session.createQuery(hql);
            List<Integer> objectList= query.list();

            System.out.println(objectList.get(2).intValue());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {

                session.close();
            }
        }
        return true; }
    public boolean deleteUser (String userId) { return true;}

    boolean addUser (String userId) throws SQLException {
        Session session = null;
        try {
            session = HibernateSessionFactory.getUserSessionFactory().openSession();
            session.beginTransaction();
            session.save(new User("2", true, 2, 4455));
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