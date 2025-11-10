/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.account;

import controller.auth.BaseAuthorizationController;
import dal.UserDBContext;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import model.self.User;

@WebServlet("/account/security")
public class SecurityController extends BaseAuthorizationController {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        UserDBContext db = new UserDBContext();
        String message;

        // --- Xử lý vô hiệu hóa tài khoản ---
        if ("deactivate".equals(action)) {

            // 🔒 Kiểm tra nếu là admin thì không cho phép
            boolean isAdmin = user.getRoles() != null && user.getRoles().stream()
                    .anyMatch(r -> "Admin".equalsIgnoreCase(r.getRoleName()));

            if (isAdmin) {
                message = "Bạn không thể vô hiệu hóa loại tài khoản này (Admin).";
            } else {
                boolean success = db.deactivateAccount(user.getId());
                if (success) {
                    req.getSession().invalidate(); // đăng xuất user
                    resp.sendRedirect(req.getContextPath() + "/login?message=Tài khoản đã bị vô hiệu hóa.");
                    return;
                } else {
                    message = "Không thể vô hiệu hóa tài khoản. Vui lòng thử lại.";
                }
            }

        } else {
            // --- Xử lý đổi mật khẩu ---
            String currentPass = req.getParameter("current_password");
            String newPass = req.getParameter("new_password");
            String confirmPass = req.getParameter("confirm_password");

            if (currentPass == null || newPass == null || confirmPass == null
                    || currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                message = "Vui lòng nhập đầy đủ thông tin.";
            } else if (!newPass.equals(confirmPass)) {
                message = "Mật khẩu mới và xác nhận không trùng khớp.";
            } else {
                boolean success = db.changePassword(user.getId(), currentPass, newPass);
                message = success ? "Đổi mật khẩu thành công!" : "Mật khẩu hiện tại không đúng.";
            }
        }

        req.setAttribute("message", message);
        req.setAttribute("pageTitle", "Đổi mật khẩu");
        req.setAttribute("contentPage", "/view/account/security.jsp");
        req.getRequestDispatcher("/view/include/layout.jsp").forward(req, resp);
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        req.setAttribute("pageTitle", "Đổi mật khẩu");
        req.setAttribute("contentPage", "/view/account/security.jsp");
        req.getRequestDispatcher("/view/include/layout.jsp").forward(req, resp);
    }
}