/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

/**
 *
 * @author Hiro
 */

package controller.request;

import dal.LeaveRequestDBContext;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import model.LeaveRequest;
import model.self.User;
import controller.auth.BaseAuthorizationController;
import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "ReviewRequestController", urlPatterns = {"/request/review"})
public class ReviewRequestController extends BaseAuthorizationController {

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        // ✅ Lấy danh sách đơn nghỉ phép của cấp dưới (dành cho Manager)
        LeaveRequestDBContext db = new LeaveRequestDBContext();
        ArrayList<LeaveRequest> requests = db.getRequestsOfSubordinates(user.getEmployee().getId());

        // Gửi dữ liệu sang JSP
        req.setAttribute("requests", requests);

        // ✅ Chuyển hướng đến trang JSP hiển thị danh sách
        req.getRequestDispatcher("../view/request/review.jsp").forward(req, resp);
    }

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        // ✅ Nhận dữ liệu duyệt / từ chối từ form
        int reqid = Integer.parseInt(req.getParameter("reqid"));
        String action = req.getParameter("action"); // "Approved" hoặc "Rejected"
        String reason = req.getParameter("reason"); // Lý do duyệt / từ chối (nếu có)

        LeaveRequestDBContext db = new LeaveRequestDBContext();
        db.reviewLeaveRequest(reqid, user.getEmployee().getId(), action, reason);

        // ✅ Quay lại trang review sau khi xử lý xong
        resp.sendRedirect("review");
    }
}
