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
import java.text.SimpleDateFormat;

@WebServlet("/request/create")
public class CreateLeaveRequestController extends BaseAuthorizationController {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        // Hiển thị trang tạo request
        req.getRequestDispatcher("/view/request/create.jsp").forward(req, resp);
    }

    @Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp, User user)
        throws ServletException, IOException {
    try {
        int typeId = Integer.parseInt(req.getParameter("typeId"));
        java.sql.Date startDate = java.sql.Date.valueOf(req.getParameter("startDate"));
        java.sql.Date endDate = java.sql.Date.valueOf(req.getParameter("endDate"));
        String reason = req.getParameter("reason");

        // Tính số ngày nghỉ
        long diff = endDate.getTime() - startDate.getTime();
        double numDays = (diff / (1000 * 60 * 60 * 24)) + 1;

        // Tạo đối tượng LeaveRequest
        LeaveRequest lr = new LeaveRequest();
        lr.setEid(user.getEmployee().getId()); // lấy id nhân viên từ user
        lr.setTypeid(typeId);
        lr.setStartDate(startDate);
        lr.setEndDate(endDate);
        lr.setNumDays(numDays);
        lr.setReason(reason);

        // Gọi DBContext để insert
        LeaveRequestDBContext db = new LeaveRequestDBContext();
        db.insert(lr);

        resp.sendRedirect(req.getContextPath() + "/request/list");
    } catch (Exception e) {
        e.printStackTrace();
        resp.getWriter().println("Lỗi khi gửi đơn: " + e.getMessage());
    }
}

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
