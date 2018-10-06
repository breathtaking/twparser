package com.twparser.dao;

import java.sql.SQLException;

/**
 * Created by
 */
public class DataBaseInteractionManager {
    public static HibernateEntityFactory entityFactory;

    public static void main(String[] args) throws SQLException {
        entityFactory = HibernateEntityFactory.getInstance();
        entityFactory.getUserDao().addUser("d");
    }
}
