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
@WebServlet(urlPatterns = {"/ResetPasswordServlet"})
public class ResetPasswordServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");
        String newPassword = request.getParameter("password");

        try (Connection conn = DatabaseConnection.initializeDatabase()) {
            String sql = "UPDATE users SET password = ?, reset_token = NULL WHERE reset_token = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, newPassword); // Ideally, hash the password
                stmt.setString(2, token);
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    request.setAttribute("message", "Your password has been successfully reset.");
                    request.getRequestDispatcher("index.html").forward(request, response);
                } else {
                    request.setAttribute("error", "Invalid or expired token.");
                    request.getRequestDispatcher("reset_password.jsp").forward(request, response);
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
