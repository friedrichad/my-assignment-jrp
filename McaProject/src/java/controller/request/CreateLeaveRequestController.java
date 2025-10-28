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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        try {
            int eid = user.getId(); // Lấy từ session
            int typeid = Integer.parseInt(req.getParameter("typeid"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date start = sdf.parse(req.getParameter("start_date"));
            java.util.Date end = sdf.parse(req.getParameter("end_date"));
            
            double numDays = (end.getTime() - start.getTime()) / (1000.0 * 60 * 60 * 24) + 1;

            LeaveRequest lr = new LeaveRequest();
            lr.setEid(eid);
            lr.setTypeid(typeid);
            lr.setStartDate(start);
            lr.setEndDate(end);
            lr.setNumDays(numDays);

            LeaveRequestDBContext db = new LeaveRequestDBContext();
            db.insert(lr);

            req.setAttribute("message", "Đơn xin nghỉ đã được tạo thành công!");
        } catch (Exception e) {
            req.setAttribute("message", "Lỗi khi tạo đơn nghỉ: " + e.getMessage());
        }
        req.getRequestDispatcher("/view/request/create.jsp").forward(req, resp);
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
