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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
//import java.io.PrintWriter;
//import static java.lang.System.out;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

/**
 *
 * @author Dzunani
 */
@WebServlet(urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Get parameters from the login form
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            // Validate user credentials
            if (isValidUser(username, password)) {
                // Set username in session for personalized content
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                // Check if the user is admin
                if (username.equals("admin") && password.equals("admin")) {
                    // Redirect to admin.jsp for admin
                    response.sendRedirect("admin.jsp");
                } else {
                // Redirect to home.jsp for regular users
                response.sendRedirect("Home.jsp");
                }    
            } else {
                request.setAttribute("errorMessage", "Incorrect username or password.");
                response.sendRedirect("index.html?error=" + URLEncoder.encode("Incorrect username or password.", StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
            
        }
    }

    // Method to validate user credentials
    private boolean isValidUser(String username, String password) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/forumapplication?zeroDateTimeBehavior=CONVERT_TO_NULL", "root", "Dzunani_03@Rik21!");
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // If result set has next, user is valid
            }
        }
    }

}