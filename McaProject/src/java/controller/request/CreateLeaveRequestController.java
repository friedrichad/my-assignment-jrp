/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.request;

import controller.auth.BaseAuthorizationController;
import dal.LeaveRequestDBContext;
import model.LeaveRequest;
import model.self.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import model.LeaveType;

@WebServlet("/request/create")
public class CreateLeaveRequestController extends BaseAuthorizationController {

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        // ✅ Nạp danh sách loại nghỉ để hiển thị trong form
        loadLeaveTypes(req);
        req.setAttribute("pageTitle", "Tạo đơn nghỉ phép");
        req.setAttribute("contentPage", "/view/request/create.jsp");
        req.getRequestDispatcher("/view/include/layout.jsp").forward(req, resp);

    }

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        try {
            int typeid = Integer.parseInt(req.getParameter("typeid"));
            java.sql.Date startDate = java.sql.Date.valueOf(req.getParameter("startDate"));
            java.sql.Date endDate = java.sql.Date.valueOf(req.getParameter("endDate"));
            String reason = req.getParameter("reason");

            // ✅ Tính số ngày nghỉ
            long diff = endDate.getTime() - startDate.getTime();
            double numDays = (diff / (1000 * 60 * 60 * 24)) + 1;

            if (numDays > 99.99) {
                loadLeaveTypes(req);
                req.setAttribute("error", "Leave duration exceeds the maximum allowed (99 days).");
                req.getRequestDispatcher("/view/request/create.jsp").forward(req, resp);
                return;
            }

            // ✅ Tạo đối tượng LeaveRequest
            LeaveRequest lr = new LeaveRequest();
            lr.setEid(user.getEmployee().getId()); // lấy ID nhân viên từ user đã đăng nhập
            lr.setTypeid(typeid);
            lr.setStartDate(startDate);
            lr.setEndDate(endDate);
            lr.setNumDays(numDays);
            lr.setReason(reason);

            // ✅ Gọi DBContext để insert
            LeaveRequestDBContext db = new LeaveRequestDBContext();
            db.insert(lr);

            // ✅ Quay lại trang danh sách
            resp.sendRedirect(req.getContextPath() + "/request/list");

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Lỗi khi gửi đơn: " + e.getMessage());
        }
    }

    private void loadLeaveTypes(HttpServletRequest req) {
        LeaveRequestDBContext ltDao = new LeaveRequestDBContext();
        ArrayList<LeaveType> leaveTypes = ltDao.listLeaveType();
        req.setAttribute("leaveTypes", leaveTypes);
    }
}
