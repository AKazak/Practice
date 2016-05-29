package by.bsu.fpmi.up.webcha.servlets;


import by.bsu.fpmi.up.webcha.storage.UserStorage;
import by.bsu.fpmi.up.webcha.utils.Hashcode;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@WebServlet(value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String login = req.getParameter("login");
            String password = Hashcode.encryptPassword(req.getParameter("password"));
            UserStorage userStorage = UserStorage.getInstance();
            req.setAttribute("login", login);
            if (userStorage.checkUser(login, password)) {
                resp.sendRedirect("/Homepage.html");
            } else {
                req.setAttribute("error", "These credentials do not match our records.");
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login.jsp");
                dispatcher.forward(req, resp);
            }
        } catch (NoSuchAlgorithmException e) {
            resp.sendError(500, e.getMessage());
        }
    }
}
