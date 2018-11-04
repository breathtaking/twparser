package com.twparser.dao;

import com.twparser.model.Post;
import com.twparser.model.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by
 */
public class DataBaseInteractionManager {
    private static HibernateEntityFactory entityFactory;

    public boolean addUser(User user) {
        entityFactory = HibernateEntityFactory.getInstance();
        if (!isUserExistsInDataBase(user.getProfileIdentifier())) {
            try {
                entityFactory.getUserDao().addUser(user);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean addPosts(List<Post> postList) {
        entityFactory = HibernateEntityFactory.getInstance();
        entityFactory.getPostDao().addPosts(postList);
        return true;
    }

    Boolean isUserExistsInDataBase(String userId) {
        if (entityFactory == null) entityFactory = new HibernateEntityFactory();
        return entityFactory.getUserDao().isUserExistsInDataBase(userId);
    }

    public static void main(String[] args) throws SQLException {
        entityFactory = HibernateEntityFactory.getInstance();
        entityFactory.getUserDao().isUserExistsInDataBase("d");
    }
}
