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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.sql.DriverManager;

/**
 *
 * @author Dzunani
 */
@WebServlet(urlPatterns = {"/ForgotPasswordServlet"})
public class ForgotPasswordServlet extends HttpServlet {

   private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");

        try (Connection conn = DatabaseConnection.initializeDatabase()) {
            String sql = "SELECT id FROM users WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int userId = rs.getInt("id");

                        // Generate a simple reset token (could just be the user's ID)
                        String resetToken = UUID.randomUUID().toString();

                        // Update the database with the reset token
                        String updateTokenSql = "UPDATE users SET reset_token = ? WHERE id = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateTokenSql)) {
                            updateStmt.setString(1, resetToken);
                            updateStmt.setInt(2, userId);
                            updateStmt.executeUpdate();
                        }

                        // Display the reset link directly on the screen
                        String resetLink = "http://yourdomain.com/reset_password.jsp?token=" + resetToken;
                        request.setAttribute("resetLink", resetLink);
                        request.getRequestDispatcher("display_reset_link.jsp").forward(request, response);
                    } else {
                        request.setAttribute("error", "No account found with that email address.");
                        request.getRequestDispatcher("forgotPassword.jsp").forward(request, response);
                    }
                }
            }
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