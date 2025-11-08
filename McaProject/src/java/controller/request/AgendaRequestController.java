/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.request;

import controller.auth.BaseAuthorizationController;
import dal.LeaveRequestDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import model.LeaveRequest;
import model.self.User;

/**
 *
 * @author Hiro
 */
@WebServlet("/request/agenda")
public class AgendaController extends BaseAuthorizationController {

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        LeaveRequestDBContext db = new LeaveRequestDBContext();
        int eid = db.getEidByUid(user.getId()); // nếu bạn có bảng Matching (uid→eid)
        ArrayList<LeaveRequest> leaves = db.getRequestsByEmployee(eid);

        req.setAttribute("leaves", leaves);
        req.getRequestDispatcher("/view/request/agenda.jsp").forward(req, resp);
    }

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        // không dùng POST
    }
}