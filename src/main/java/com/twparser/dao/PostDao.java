package com.twparser.dao;

import com.twparser.model.Post;
import org.hibernate.Session;

import java.util.List;

/**
 * Post dao
 */
public class PostDao {
    private List<Post> postList;

    public boolean addPosts(List<Post> postList) {
        Session session = null;
        try {
            session = HibernateSessionFactory.getPostSessionFactory().openSession();
            session.beginTransaction();
            for (Post post: postList) {
                session.save(post);
                System.out.println("Post with id:" +
                post.getPostId() + " was added to db");
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return true;
    }
}
