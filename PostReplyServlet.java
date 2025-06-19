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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 *
 * @author Dzunani
 */
@WebServlet(urlPatterns = {"/PostReplyServlet"})
public class PostReplyServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/forumapplication";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Dzunani_03@Rik21!";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String text = request.getParameter("text");
        int commentId = Integer.parseInt(request.getParameter("comment_id"));
        int userId = (Integer) request.getSession().getAttribute("user_id");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "INSERT INTO replies (text, comment_id, user_id) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, text);
                stmt.setInt(2, commentId);
                stmt.setInt(3, userId);
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("topic_details.jsp?id=" + request.getParameter("topic_id"));
    }
}
