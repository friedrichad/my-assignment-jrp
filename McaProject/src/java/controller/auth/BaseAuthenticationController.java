/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.self.User;

/**
 *
 * @author hiro
 */
public abstract class BaseAuthenticationController extends HttpServlet {
   private boolean isAuthenticated(HttpServletRequest req) {
        User u = (User) req.getSession().getAttribute("auth");
        return u != null;
    }

    // abstract methods — bắt buộc lớp con phải override
    protected abstract void doGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException;

    protected abstract void doPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (isAuthenticated(req)) {
            User u = (User) req.getSession().getAttribute("auth");
            doGet(req, resp, u);
        } else {
            // ✅ sửa: forward tuyệt đối (tính từ context root)
            req.setAttribute("error", "You are not logged yet!");
            req.getRequestDispatcher("/view/auth/login.jsp").forward(req, resp);
            // hoặc có thể dùng redirect:
            // resp.sendRedirect(req.getContextPath() + "/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (isAuthenticated(req)) {
            User u = (User) req.getSession().getAttribute("auth");
            doPost(req, resp, u);
        } else {
            resp.getWriter().println("Access denied!");
        }
    }
}