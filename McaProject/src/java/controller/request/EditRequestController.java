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
import java.util.ArrayList;
import model.LeaveRequest;
import model.LeaveType;
import model.self.User;

/**
 *
 * @author Hiro
 */
@WebServlet(name = "EditRequestController", urlPatterns = {"/request/edit"})
public class EditRequestController extends BaseAuthorizationController {

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        String idRaw = req.getParameter("id");
        if (idRaw == null || idRaw.isEmpty()) {
            req.setAttribute("error", "Thiếu mã đơn nghỉ phép!");
            req.setAttribute("pageTitle", "Danh sách đơn nghỉ phép");
            req.setAttribute("contentPage", "/view/request/list.jsp");
            req.getRequestDispatcher("/view/include/layout.jsp").forward(req, resp);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idRaw);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "ID không hợp lệ!");
            req.setAttribute("pageTitle", "Danh sách đơn nghỉ phép");
            req.setAttribute("contentPage", "/view/request/list.jsp");
            req.getRequestDispatcher("/view/include/layout.jsp").forward(req, resp);
            return;
        }

        LeaveRequestDBContext dao = new LeaveRequestDBContext();
        LeaveRequest lr = dao.getById(id);
        ArrayList<LeaveType> leaveTypes = dao.getLeaveTypes();
        dao.closeConnection();

        if (lr == null) {
            req.setAttribute("error", "Không tìm thấy đơn nghỉ phép!");
            req.setAttribute("pageTitle", "Danh sách đơn nghỉ phép");
            req.setAttribute("contentPage", "/view/request/list.jsp");
            req.getRequestDispatcher("/view/include/layout.jsp").forward(req, resp);
            return;
        }

        if (!"Pending".equalsIgnoreCase(lr.getStatus())
                || lr.getEid() != user.getEmployee().getId()) {
            req.setAttribute("error", "Bạn không thể chỉnh sửa đơn này!");
            req.setAttribute("pageTitle", "Danh sách đơn nghỉ phép");
            req.setAttribute("contentPage", "/view/request/list.jsp");
            req.getRequestDispatcher("/view/include/layout.jsp").forward(req, resp);
            return;
        }

        req.setAttribute("leaveTypes", leaveTypes);
        req.setAttribute("request", lr);
        req.setAttribute("pageTitle", "Chỉnh sửa đơn nghỉ phép");
        req.setAttribute("contentPage", "/view/request/edit.jsp");
        req.getRequestDispatcher("/view/include/layout.jsp").forward(req, resp);
    }

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        try {
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

                // ✅ Redirect an toàn (không chứa Unicode/emoji)
                resp.sendRedirect(req.getContextPath() + "/request/list?success=true");
                return;

            } else {
                dao.closeConnection();
                resp.sendRedirect(req.getContextPath() + "/request/list?error=unauthorized");
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/request/list?error=exception");
        }
    }
}
