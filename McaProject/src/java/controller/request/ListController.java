/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
/**
 *
 * @author Hiro
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
import model.self.User;

@WebServlet("/request/list")
public class ListController extends BaseAuthorizationController {

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        int page = 1;
        int size = 10;

        if (req.getParameter("page") != null) {
            try {
                page = Integer.parseInt(req.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        if (req.getParameter("size") != null) {
            try {
                size = Integer.parseInt(req.getParameter("size"));
            } catch (NumberFormatException e) {
                size = 10;
            }
        }

        String status = req.getParameter("status");
        String fromStr = req.getParameter("from");
        String toStr = req.getParameter("to");

        Date fromDate = (fromStr != null && !fromStr.isEmpty()) ? Date.valueOf(fromStr) : null;
        Date toDate = (toStr != null && !toStr.isEmpty()) ? Date.valueOf(toStr) : null;

        LeaveRequestDBContext dao = new LeaveRequestDBContext();

        // ✅ Lấy danh sách đơn nghỉ (theo bộ lọc & phân trang)
        ArrayList<LeaveRequest> requests = dao.searchRequests(
                user.getEmployee().getId(),
                status,
                fromDate,
                toDate,
                page,
                size
        );
        System.out.println("Found requests: " + requests.size());
for (LeaveRequest r : requests) {
    System.out.println("Request ID: " + r.getId());
}

        // ✅ Đếm tổng số dòng để tính số trang
        int totalRecords = dao.countRequests(
                user.getEmployee().getId(),
                status,
                fromDate,
                toDate
        );

        int totalPages = (int) Math.ceil((double) totalRecords / size);

        // ✅ Tổng ngày nghỉ đã Approved
        int totalApprovedDays = dao.getTotalApprovedDays(user.getEmployee().getId());
        int remainingDays = 99 - totalApprovedDays;

        dao.closeConnection();

        req.setAttribute("requests", requests);
        req.setAttribute("page", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("size", size);
        req.setAttribute("totalApprovedDays", totalApprovedDays);
        req.setAttribute("remainingDays", remainingDays);
        req.setAttribute("statusFilter", status);
        req.setAttribute("fromDate", fromStr);
        req.setAttribute("toDate", toStr);

        req.getRequestDispatcher("/view/request/list.jsp").forward(req, resp);
    }

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        processGet(req, resp, user);
    }
}
