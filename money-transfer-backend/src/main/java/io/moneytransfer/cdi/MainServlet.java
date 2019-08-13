package io.moneytransfer.cdi;

import io.moneytransfer.validation.user.UserValidation;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

// Using Servlet 3.0+
@WebServlet(name = "MainServlet", urlPatterns = {"/"})
public class MainServlet extends HttpServlet {

    @Inject
    private Greeting greeting;
    @Inject
    private javax.enterprise.event.Event<String> event;

    @Inject
    private UserValidation userValidation;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet MainServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<p>" + greeting.getText() + "</p>");
            out.println("</body>");
            out.println("</html>");
        }

        // Test CDI event support, this event is observed by the DefaultGreeting class
        event.fire("Simple test event");
    }

}
