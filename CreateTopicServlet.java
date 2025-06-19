/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Dzunani
 */
@WebServlet(urlPatterns = {"/CreateTopicServlet"})
public class CreateTopicServlet extends HttpServlet {
     private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get title and description from form
        String title = request.getParameter("title");
        String description = request.getParameter("description");

        // Get current user's ID from session (assumes user is logged in and their ID is stored in session)
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            // Redirect to login if the user is not logged in
            response.sendRedirect("index.html");
            return;
        }

        // Insert the topic into the database
        try (Connection conn = DatabaseConnection.initializeDatabase()) {
            String sql = "INSERT INTO topics (title, description, user_id) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, title);
                stmt.setString(2, description);
                stmt.setInt(3, userId);  // Assume userId is stored in the session
                stmt.executeUpdate();
            }

            // Redirect to the all topics page after successful creation
            response.sendRedirect("AllTopicsServlet");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
   

    class DatabaseConnection {
        public static Connection initializeDatabase() throws SQLException {
        String dbDriver = "com.mysql.cj.jdbc.Driver";
        String dbURL = "jdbc:mysql://localhost:3306/";
        String dbName = "forumapplication";
        String dbUsername = "root";
        String dbPassword = "Dzunani_03@Rik21!";

        try {
            Class.forName(dbDriver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return DriverManager.getConnection(dbURL + dbName, dbUsername, dbPassword);
        }
    }

