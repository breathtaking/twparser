package com.twparser.dao;

import com.twparser.model.User;
import org.hibernate.Session;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Performs DB interaction for User entity
 */

public class UserDao {
    public User getUser (String userId) { return null; }
    public List<User> getAllUsers() { return null;}
    public boolean checkUser (String userId) { return true; }
    public boolean deleteUser (String userId) { return true;}

    public boolean addUser (String userId) throws SQLException {
        Session session = null;
        try {
            session = HibernateSessionFactory.getUserSessionFactory().openSession();
            session.beginTransaction();
            session.save(new User("2", true, 2, 4455));
            session.getTransaction().commit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка при вставке", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {

                session.close();
            }
        }
        return true;
    }
    public boolean refreshStatus (String userId) { return true; }

}