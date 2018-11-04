package com.twparser.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.twparser.model.Post;
import com.twparser.model.User;
import com.twparser.processing.OperationManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller that handles requests from Admin Control Panel
 */
@WebServlet(name = "AdministratorPanelHandler", urlPatterns = "/adduser")
public class AdministratorPanelHandler extends HttpServlet {
    private OperationManager operationManager = new OperationManager();
    private List<Post> posts = null;
    private User user;
    private int page;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getParameter("page") == null) {
            req.getRequestDispatcher("/WEB-INF/pages/new_user_page.jsp").forward(req, resp);
        }

        int recordsPerPage = 10;
        if(req.getParameter("page") != null) page = Integer.parseInt(req.getParameter("page"));
        int noOfRecords = posts.size();
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

        List<Post> sorted = new ArrayList<>();
        sorted.addAll(posts);
        int checker = (posts.size() - 1) - (page - 1) * recordsPerPage;
        int endIndex = (page - 1) * recordsPerPage + (recordsPerPage);
        if (checker < recordsPerPage) { endIndex = (page - 1) * recordsPerPage + checker + 1; }
        List<Post> sublist = sorted.subList((page - 1) * recordsPerPage, endIndex);

        req.setAttribute("postList", sublist);
        req.setAttribute("noOfPages", noOfPages);
        req.setAttribute("currentPage", page);
        req.setAttribute("user", user);
        req.getRequestDispatcher("/WEB-INF/pages/content.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userUrl = req.getParameter("userUrl");

        try {
            posts = operationManager.addUser(userUrl);
            user = operationManager.grabUser(userUrl);
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        page = 1;
        int recordsPerPage = 10;
        int numberOfPosts = posts.size();
        int numberOfPages = (int) Math.ceil(numberOfPosts * 1.0 / recordsPerPage);


        List<Post> sorted = new ArrayList<>();
        sorted.addAll(posts);
        List<Post> sublist = sorted.subList(0 , recordsPerPage);

        req.setAttribute("postList", sublist);
        req.setAttribute("noOfPages", numberOfPages);
        req.setAttribute("currentPage", page);
        req.setAttribute("user", user);
        req.getRequestDispatcher("/WEB-INF/pages/content.jsp").forward(req, resp);
    }
}
