/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.request;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import dal.LeaveRequestDBContext;
import model.LeaveRequest;
import controller.auth.BaseAuthorizationController;
import dal.EmployeeDBContext;
import java.util.ArrayList;
import model.self.User;


@WebServlet("/request/delete")
public class DeleteRequestController extends BaseAuthorizationController {

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        LeaveRequestDBContext db = new LeaveRequestDBContext();

        if ("delete".equals(action)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                LeaveRequest lr = db.get(id);

                // Chỉ xóa nếu đơn đang chờ duyệt
                if (lr != null && "Pending".equalsIgnoreCase(lr.getStatus())) {
                    db.delete(id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Sau khi xóa xong, trở lại danh sách (refresh page)
        resp.sendRedirect(req.getContextPath() + "/request/list");
    }

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}