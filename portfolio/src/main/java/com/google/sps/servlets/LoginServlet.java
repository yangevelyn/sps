package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    response.setContentType("text/html");
    if(userService.isUserLoggedIn()) {String email = userService.getCurrentUser().getEmail();
      String logout = userService.createLogoutURL("/");

      response.getWriter().println("<p>" + email + " is logged in.<p>");
      response.getWriter().println("<p>Log out <a href=\"" + logout + "\">here</a><p>");

      response.getWriter().println("<form action=\"/comment\" method=\"POST\">");
      response.getWriter().println("<p>Feel free to leave your comments here!</p>");
      response.getWriter().println("<textarea name=\"name\" placeholder=\"Name\"></textarea>");
      response.getWriter().println("<br/>");
      response.getWriter().println("<textarea name=\"comment\" rows=\"10\" cols=\"50\" placeholder=\"Comment\"></textarea>");
      response.getWriter().println("<br/>");  
      response.getWriter().println("<input type=\"submit\" />");  
      response.getWriter().println("</form>");
    } else {String login = userService.createLoginURL("/");

      response.getWriter().println("<p>Log in <a href=\"" + login + "\">here</a><p>");
    }
  }
}