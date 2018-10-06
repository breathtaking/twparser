package com.twparser.dao;

/**
 * Factory which instantiate Dao classes
 */
public class HibernateEntityFactory {
    private static UserDao userDao = null;
    private static PostDao postDao = null;
    private static HibernateEntityFactory instance = null;

    public static synchronized HibernateEntityFactory getInstance() {
        if (instance == null) {
            instance = new HibernateEntityFactory();
        }
        return instance;
    }

    public UserDao getUserDao() {
        if (userDao == null) {
            userDao = new UserDao(); }
        return userDao;
    }

    public PostDao getPostDao(){
        if (postDao == null) {
            postDao = new PostDao(); }
        return postDao;
    }
}
