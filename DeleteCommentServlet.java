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
@WebServlet(urlPatterns = {"/DeleteCommentServlet"})
public class DeleteCommentServlet extends HttpServlet {

   private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String commentId = request.getParameter("id");

        if (commentId == null || commentId.isEmpty()) {
            response.sendRedirect("admin.jsp");
            return;
        }

        try (Connection conn = DatabaseConnection.initializeDatabase()) {

            // Delete associated replies first (if any)
            String deleteRepliesSQL = "DELETE FROM replies WHERE comment_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteRepliesSQL)) {
                stmt.setInt(1, Integer.parseInt(commentId));
                stmt.executeUpdate();
            }

            // Delete the comment
            String deleteCommentSQL = "DELETE FROM comments WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteCommentSQL)) {
                stmt.setInt(1, Integer.parseInt(commentId));
                stmt.executeUpdate();
            }

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