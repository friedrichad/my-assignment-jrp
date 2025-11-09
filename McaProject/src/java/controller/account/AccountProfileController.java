/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.account;

import controller.auth.BaseAuthorizationController;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import model.self.User;
import model.Employee; // nếu bạn có model riêng cho Employee
import dal.EmployeeDBContext; // để lấy thông tin nhân viên từ DB

/**
 *
 * @author Hiro
 */
@WebServlet("/account/profile")
public class AccountProfileController extends BaseAuthorizationController {

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        try {
            EmployeeDBContext db = new EmployeeDBContext();
            int eid = user.getEmployee().getId(); // Lấy employee ID từ user đăng nhập
            Employee emp = db.getEmployeeById(eid);

            req.setAttribute("employee", emp);
            req.setAttribute("pageTitle", "Thông tin cá nhân");
            req.setAttribute("contentPage", "/view/account/profile.jsp");

            req.getRequestDispatcher("/view/include/layout.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Lỗi khi tải thông tin cá nhân: " + e.getMessage());
        }
    }

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        // Nếu muốn cho phép chỉnh sửa thông tin cá nhân sau này
    }
}