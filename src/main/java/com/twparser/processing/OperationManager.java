package com.twparser.processing;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.twparser.controller.prototype.UnirestScrapperTW;
import com.twparser.dao.DataBaseInteractionManager;
import com.twparser.model.Post;
import com.twparser.model.User;

import java.util.List;

/**
 * Interacts with databace and http connection
 */
public class OperationManager {
    private DataBaseInteractionManager dataBaseInteractionManager = new DataBaseInteractionManager();

    public static void main(String[] args) {
        OperationManager operationManager = new OperationManager();
        try {
            operationManager.addUser("kendricklamar");
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    public List<Post> addUser(String userId) throws UnirestException {
        System.out.println("Receiving user data...");
        User user = UnirestScrapperTW.grabUser(userId);

        System.out.println("Receiving tweets...");
        List<Post> postList = UnirestScrapperTW.grabAllPosts(userId);

        if(postList != null && postList.size() > 0) {
            System.out.println("Adding posts to database");
            addPosts(postList);
        }

        System.out.println("Checking user " + userId + " in database...");
        boolean isUserExist = dataBaseInteractionManager.addUser(user);
        System.out.println("Was user added?" + isUserExist);

        System.out.println("Success!");
        return postList;
    }

    private boolean addPosts (List<Post> postList) {
        return dataBaseInteractionManager.addPosts(postList);
    }

    public User grabUser (String userId) {
        return UnirestScrapperTW.grabUser(userId);
    }

}
