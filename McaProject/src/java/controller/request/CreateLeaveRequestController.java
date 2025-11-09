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
import model.self.Role;

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
            String reason = req.getParameter("reason").trim();

            // ✅ Tính số ngày nghỉ
            long diff = endDate.getTime() - startDate.getTime();
            double numDays = (diff / (1000 * 60 * 60 * 24)) + 1;

            LeaveRequestDBContext db = new LeaveRequestDBContext();

            // ⚙️ Kiểm tra logic hợp lệ
            if (startDate.after(endDate)) {
                reloadFormWithError(req, resp, "❌ Ngày bắt đầu không thể sau ngày kết thúc.");
                return;
            }

            if (numDays > 30) {
                reloadFormWithError(req, resp, "⚠️ Không được nghỉ quá 30 ngày liên tiếp.");
                return;
            }

            if (reason.isEmpty()) {
                reloadFormWithError(req, resp, "⚠️ Vui lòng nhập lý do xin nghỉ.");
                return;
            }

            boolean overlap = db.checkOverlapLeave(user.getEmployee().getId(), startDate, endDate);
            if (overlap) {
                reloadFormWithError(req, resp, "⚠️ Bạn đã có đơn xin nghỉ trùng thời gian này.");
                return;
            }

            // ✅ Tạo đối tượng LeaveRequest
            LeaveRequest lr = new LeaveRequest();
            lr.setEid(user.getEmployee().getId());
            lr.setTypeid(typeid);
            lr.setStartDate(startDate);
            lr.setEndDate(endDate);
            lr.setNumDays(numDays);
            lr.setReason(reason);

            // ✅ Kiểm tra role - Admin thì auto Approved
            System.out.println(">>> [DEBUG] Kiểm tra quyền user trước khi insert:");
            if (user.getRoles() != null) {
                for (Role r : user.getRoles()) {
                    System.out.println(" - Role: " + r.getRoleName());
                }
            }

            boolean isAdmin = user.hasRole("Admin"); // đảm bảo hàm này dùng equalsIgnoreCase
            System.out.println(">>> [DEBUG] hasRole('Admin') = " + isAdmin);

            if (isAdmin) {
                lr.setStatus("Approved");
                System.out.println(">>> [DEBUG] Admin detected → Đơn tự động duyệt!");
            } else {
                lr.setStatus("Pending");
            }

            // ✅ Insert vào DB
            db.insert(lr);
            System.out.println("✅ Insert thành công đơn nghỉ phép (eid=" + lr.getEid() + ", status=" + lr.getStatus() + ")");

            // ✅ Quay lại danh sách
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

    private void reloadFormWithError(HttpServletRequest req, HttpServletResponse resp, String message)
            throws ServletException, IOException {
        loadLeaveTypes(req);
        req.setAttribute("error", message);
        req.setAttribute("pageTitle", "Tạo đơn nghỉ phép");
        req.setAttribute("contentPage", "/view/request/create.jsp");
        req.getRequestDispatcher("/view/include/layout.jsp").forward(req, resp);
    }
}