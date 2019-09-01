package info.mastera;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestApp extends HttpServlet {
    public String getGreeting() {
        return "Hello world.";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html");
        try {
            response.getWriter().println(getGreeting());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}