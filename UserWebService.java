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

/**
 *
 * @author Dzunani
 */
@WebServlet(urlPatterns = {"/UserWebService"})
public class UserWebService extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Create a new user
        String action = request.getPathInfo();
        if ("/create".equals(action)) {
            createUser(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetch user details
        String action = request.getPathInfo();
        if (action != null && action.startsWith("/details/")) {
            int userId = Integer.parseInt(action.substring("/details/".length()));
            getUserById(userId, response);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Update user details
        String action = request.getPathInfo();
        if ("/update".equals(action)) {
            updateUser(request, response);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Delete user
        String action = request.getPathInfo();
        if (action != null && action.startsWith("/delete/")) {
            int userId = Integer.parseInt(action.substring("/delete/".length()));
            deleteUser(userId, response);
        }
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection conn = DatabaseConnection.initializeDatabase()) {
            String sql = "INSERT INTO Users (username, password) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password); // Hash the password in a real app
                stmt.executeUpdate();

                // Respond with success message in plain text
                PrintWriter out = response.getWriter();
                response.setContentType("text/plain");
                out.println("User created successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = response.getWriter();
            out.println("Error creating user");
        }
    }

    private void getUserById(int userId, HttpServletResponse response) throws IOException {
        try (Connection conn = DatabaseConnection.initializeDatabase()) {
            String sql = "SELECT id, username FROM users WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        PrintWriter out = response.getWriter();
                        response.setContentType("text/plain");
                        out.println("User ID: " + rs.getInt("id"));
                        out.println("Username: " + rs.getString("username"));
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        PrintWriter out = response.getWriter();
                        out.println("User not found");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = response.getWriter();
            out.println("Error fetching user");
        }
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        int userId = Integer.parseInt(request.getParameter("id"));

        try (Connection conn = DatabaseConnection.initializeDatabase()) {
            String sql = "UPDATE users SET username = ?, password = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password); // Hash the password in a real app
                stmt.setInt(4, userId);

                int rowsAffected = stmt.executeUpdate();

                PrintWriter out = response.getWriter();
                response.setContentType("text/plain");

                if (rowsAffected > 0) {
                    out.println("User updated successfully");
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.println("User not found");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = response.getWriter();
            out.println("Error updating user");
        }
    }

    private void deleteUser(int userId, HttpServletResponse response) throws IOException {
        try (Connection conn = DatabaseConnection.initializeDatabase()) {
            String sql = "DELETE FROM users WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);

                int rowsAffected = stmt.executeUpdate();

                PrintWriter out = response.getWriter();
                response.setContentType("text/plain");

                if (rowsAffected > 0) {
                    out.println("User deleted successfully");
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.println("User not found");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = response.getWriter();
            out.println("Error deleting user");
        }
    }
}