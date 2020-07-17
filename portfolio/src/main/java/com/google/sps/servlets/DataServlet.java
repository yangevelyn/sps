// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import com.google.gson.Gson;
import java.util.Arrays;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


/*
 * Handles user input in comment boxes and displays existing comments from Datastore
 */
@WebServlet("/comment")
public class DataServlet extends HttpServlet {

  ArrayList<String> comments = new ArrayList<String>();

  /*
   * Stores a user comment in Datastore and redirects back to same page
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
    UserService userService = UserServiceFactory.getUserService();
    String email = userService.getCurrentUser().getEmail();
    
    String name = request.getParameter("name");
    String comment = request.getParameter("comment");
    comments.add(comment);

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("name", name);
    commentEntity.setProperty("email", email);
    commentEntity.setProperty("content", comment);
    commentEntity.setProperty("timestamp", System.currentTimeMillis());

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    response.sendRedirect("/");
  }

  /*
   * Displays all existing user comments, sorted by time commented
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    ArrayList<Object> dataList = new ArrayList<>();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query("Comment").addSort("timestamp");
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      Object[] item = {entity.getProperty("email"), entity.getProperty("content")};
      dataList.add(item);
    }

    String json = new Gson().toJson(dataList);

    response.setContentType("text/html");
    response.getWriter().println(json);
  }
}
