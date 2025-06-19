/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import jakarta.servlet.RequestDispatcher;
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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dzunani
 */
@WebServlet(urlPatterns = {"/AllTopicsServlet"})
public class AllTopicsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Topic> topics = new ArrayList<>();

        try (Connection conn = DatabaseConnection.initializeDatabase()) {
            String sql = "SELECT t.id, t.title, t.description, u.name AS userName FROM topics t "
                       + "JOIN Users u ON t.user_id = u.id";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Topic topic = new Topic();
                        topic.setId(rs.getInt("id"));
                        topic.setTitle(rs.getString("title"));
                        topic.setDescription(rs.getString("description"));
                        topic.setUsername(rs.getString("username"));
                        topics.add(topic);
                    }
                }
            }

            // Set the topics as a request attribute
            request.setAttribute("topics", topics);

            // Forward to all_topics.jsp
            RequestDispatcher dispatcher = request.getRequestDispatcher("all_topics.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
    