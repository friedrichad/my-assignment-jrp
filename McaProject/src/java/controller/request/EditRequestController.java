/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.request;

import controller.auth.BaseAuthorizationController;
import dal.LeaveRequestDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import model.LeaveRequest;
import model.self.User;

/**
 *
 * @author Hiro
 */
@WebServlet(name = "EditRequestController", urlPatterns = {"/request/edit"})
public class EditRequestController extends BaseAuthorizationController {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        String idRaw = req.getParameter("id");
        if (idRaw == null || idRaw.isEmpty()) {
            req.setAttribute("error", "❌ Thiếu mã đơn nghỉ phép!");
            req.getRequestDispatcher("/request/list").forward(req, resp);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idRaw);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "❌ ID không hợp lệ!");
            req.getRequestDispatcher("/request/list").forward(req, resp);
            return;
        }

        LeaveRequestDBContext dao = new LeaveRequestDBContext();
        LeaveRequest lr = dao.getById(id);
        dao.closeConnection();

        if (lr == null) {
            req.setAttribute("error", "❌ Không tìm thấy đơn nghỉ phép!");
            req.getRequestDispatcher("/request/list").forward(req, resp);
            return;
        }

        // Chỉ cho phép chỉnh sửa nếu Pending và đúng user
        if (!"Pending".equalsIgnoreCase(lr.getStatus())
                || lr.getEid() != user.getEmployee().getId()) {
            req.setAttribute("error", "🚫 Bạn không thể chỉnh sửa đơn này!");
            req.getRequestDispatcher("/request/list").forward(req, resp);
            return;
        }

        req.setAttribute("request", lr);
        req.getRequestDispatcher("/view/request/edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        int reqid = Integer.parseInt(req.getParameter("reqid"));
        String reason = req.getParameter("reason");
        Date startDate = Date.valueOf(req.getParameter("startDate"));
        Date endDate = Date.valueOf(req.getParameter("endDate"));

        LeaveRequestDBContext dao = new LeaveRequestDBContext();
        LeaveRequest current = dao.getById(reqid);

        if (current != null
                && "Pending".equalsIgnoreCase(current.getStatus())
                && current.getEid() == user.getEmployee().getId()) {

            current.setStartDate(startDate);
            current.setEndDate(endDate);
            current.setReason(reason);
            dao.updatePendingRequest(current);
            dao.closeConnection();

            // Redirect với thông báo thành công
            resp.sendRedirect(req.getContextPath() + "/request/list?success=✅ Cập nhật thành công!");
        } else {
            // Redirect với thông báo lỗi
            resp.sendRedirect(req.getContextPath() + "/request/list?error=🚫 Không thể cập nhật đơn này!");
        }

        // Load lại danh sách để hiển thị thông báo
        req.getRequestDispatcher("/request/list").forward(req, resp);
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
