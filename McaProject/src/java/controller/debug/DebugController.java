/*
 * Simple controller to display roles, features, and recent logs.
 */
package controller;

import dal.RoleDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import model.self.Role;
import model.self.User;

@WebServlet("/debug")
public class DebugController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Giả sử user đã login và lưu trong session
        User user = (User) request.getSession().getAttribute("auth");

        if (user == null) {
            response.getWriter().println("<h3 style='color:red'>User chưa đăng nhập!</h3>");
            return;
        }

        // Gọi DB để lấy lại roles
        RoleDBContext db = new RoleDBContext();
        ArrayList<Role> roles = db.getByUserId(user.getId());
        user.setRoles(roles);
        request.getSession().setAttribute("auth", user); // cập nhật lại session

        // Giả lập log session (bạn có thể thay bằng log thực từ file)
        ArrayList<String> logs = (ArrayList<String>) getServletContext().getAttribute("DEBUG_LOGS");
        if (logs == null) {
            logs = new ArrayList<>();
            logs.add("No logs yet.");
        }

        // Gửi data sang JSP
        request.setAttribute("roles", roles);
        request.setAttribute("logs", logs);
        request.setAttribute("user", user);

        request.getRequestDispatcher("view/debug.jsp").forward(request, response);
    }
}
