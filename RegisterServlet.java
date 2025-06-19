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
import static java.lang.System.out;

//import static java.lang.System.out;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Dzunani
 */
@WebServlet(urlPatterns = {"/RegisterServlet"})
public class RegisterServlet extends HttpServlet {

 @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw=response.getWriter();
        //Get parameters from the registration form
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        try
        {
            //Add the username and password to the datbase table userdetails
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/forumapplication","root","Dzunani_03@Rik21!");
            PreparedStatement ps = con.prepareStatement("INSERT INTO users(username, password) VALUES(?,?)");
            ps.setString(1, username);
            ps.setString(2, password);
            int i=ps.executeUpdate();
            //It inputted successfully the user is redirected to the login page to login
            if (i > 0) {
                response.sendRedirect("index.html");
            } else {
                pw.println("Some Error Occurred, try again later");
            }
            ps.close();
            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            // Handle exceptions
            e.printStackTrace();
            out.println("An error occurred: " + e.getMessage());
        }
    }
    


}