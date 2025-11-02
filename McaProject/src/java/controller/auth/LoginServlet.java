/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.auth;

import dal.UserDBContext;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import model.self.User;

/**
 *
 * @author Hiro
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Nếu đã đăng nhập -> chuyển về trang chủ
        HttpSession session = request.getSession();
        if (session.getAttribute("auth") != null) {
            response.sendRedirect("home");
        } else {
            request.getRequestDispatcher("view/auth/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String account = request.getParameter("account");
        String password = request.getParameter("password");

        UserDBContext userDB = new UserDBContext();
        User user = userDB.getUserByLogin(account, password);
        userDB.closeConnection(); // Đóng kết nối DB

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("auth", user);
            response.sendRedirect("home");
        } else {
            request.setAttribute("error", "Incorrect username or password!");
            request.getRequestDispatcher("view/auth/login.jsp").forward(request, response);

        }
    }
}
