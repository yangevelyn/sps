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
    if(userService.isUserLoggedIn()) {
      String email = userService.getCurrentUser().getEmail();
      String logout = userService.createLogoutURL("/");

      response.getWriter().println("<p>" + email + " is logged in.<p>");
      response.getWriter().println("<p>Log out <a href=\"" + logout + "\">here</a><p>");
    } else {
      String login = userService.createLoginURL("/");

      response.getWriter().println("<p>Log in <a href=\"" + login + "\">here</a><p>");
    }
  }
}