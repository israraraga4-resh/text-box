package com.cookieservlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/CookieServlet")
public class CookieServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Handle GET request
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String existingUser = null;
        int count = 0;

        // Read cookies
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user")) {
                    existingUser = cookie.getValue();
                }
                if (cookie.getName().equals("count")) {
                    try {
                        count = Integer.parseInt(cookie.getValue());
                    } catch (Exception e) {
                        count = 0;
                    }
                }
            }
        }

        out.println("<html>");
        out.println("<head><title>Cookie Example</title></head>");
        out.println("<body>");

        if (existingUser != null && !existingUser.isEmpty()) {

            // Increase visit count
            count++;

            // Update count cookie
            Cookie countCookie = new Cookie("count", String.valueOf(count));
            countCookie.setMaxAge(60); // 1 minute
            response.addCookie(countCookie);

            out.println("<font color=blue><h2>Welcome back, " + existingUser + "!</h2></font>");
            out.println("<font color=magenta><h2>You have visited this page " + count + " times!</h2></font>");

            // Logout button
            out.println("<form action='CookieServlet' method='post'>");
            out.println("<input type='submit' value='Logout'>");
            out.println("</form>");

        } else {
            // First-time user form
            out.println("<h2 style='color:red;'>Welcome Guest! Please login</h2>");
            out.println("<form action='CookieServlet' method='post'>");
            out.println("Enter your name: <input type='text' name='userName'>");
            out.println("<input type='submit' value='Submit'>");
            out.println("</form>");
        }

        out.println("</body></html>");
    }

    // Handle POST request
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userName = request.getParameter("userName");

        if (userName != null && !userName.isEmpty()) {
            // LOGIN: create cookies

            Cookie userCookie = new Cookie("user", userName);
            userCookie.setMaxAge(60); // 1 minute

            Cookie countCookie = new Cookie("count", "0");
            countCookie.setMaxAge(60);

            response.addCookie(userCookie);
            response.addCookie(countCookie);

        } else {
            // LOGOUT: delete cookies

            Cookie userCookie = new Cookie("user", "");
            userCookie.setMaxAge(0);

            Cookie countCookie = new Cookie("count", "");
            countCookie.setMaxAge(0);

            response.addCookie(userCookie);
            response.addCookie(countCookie);
        }

        // Redirect to GET
        response.sendRedirect("CookieServlet");
    }
}