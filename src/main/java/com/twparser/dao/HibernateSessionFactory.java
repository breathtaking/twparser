package com.twparser.dao;

import com.twparser.model.User;
import com.twparser.model.Post;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by BRVRS
 */
public class HibernateSessionFactory {
    private static final SessionFactory userSessionFactory;
    private static final SessionFactory postSessionFactory;

    static {
        try {
            //addPackage("com.xyz") //add package if used after configure.
            userSessionFactory = new Configuration().
                    configure().addAnnotatedClass(User.class).buildSessionFactory();
            postSessionFactory = new Configuration().
                    configure().addAnnotatedClass(Post.class).buildSessionFactory();

        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getUserSessionFactory() {
        return userSessionFactory;
    }

    public static SessionFactory getPostSessionFactory() {
        return userSessionFactory;
    }
}
