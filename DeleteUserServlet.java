/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Dzunani
 */
@WebServlet(urlPatterns = {"/DeleteUserServlet"})
public class DeleteUserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("id");

        if (userId == null || userId.isEmpty()) {
            response.sendRedirect("admin.jsp");
            return;
        }

        try (Connection conn = DatabaseConnection.initializeDatabase()) {

            // Delete the user from the database
            String sql = "DELETE FROM users WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(userId));
                stmt.executeUpdate();
            }

            // Optionally, delete related data (topics, comments, etc.) here

            // Redirect back to the admin page
            response.sendRedirect("admin.jsp");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    
private static class DatabaseConnection {

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
        
    }