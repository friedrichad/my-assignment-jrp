/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.dashboard;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import model.Employee;
import model.self.User;
import controller.auth.BaseAuthorizationController;
import dal.EmployeeDBContext;
import model.Division;

/**
 *
 * @author Hiro
 */
@WebServlet("/controller/dashboard")
public class DashboardController extends BaseAuthorizationController {

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        System.out.println("=== [DashboardController] processGet START ===");
        System.out.println("User: " + (user != null ? user.getDisname() : "null"));
        System.out.println("Employee ID: " +
                (user != null && user.getEmployee() != null ? user.getEmployee().getId() : "null"));

        // --- Lấy parameter tìm kiếm, lọc, phân trang ---
        String search = req.getParameter("search");
        String rawDivision = req.getParameter("division");
        String rawPage = req.getParameter("page");

        int pageIndex = 1;
        int pageSize = 10;
        Integer divisionId = null;

        if (rawDivision != null && !rawDivision.isEmpty()) {
            try {
                divisionId = Integer.parseInt(rawDivision);
            } catch (NumberFormatException e) {
                divisionId = null;
            }
        }

        if (rawPage != null && !rawPage.isEmpty()) {
            try {
                pageIndex = Integer.parseInt(rawPage);
            } catch (NumberFormatException e) {
                pageIndex = 1;
            }
        }

        // --- Lấy dữ liệu từ DB ---
        EmployeeDBContext empDB = new EmployeeDBContext();

        ArrayList<Employee> employees = empDB.getEmployees(search, divisionId, pageIndex, pageSize);
        ArrayList<Division> divisions = empDB.getAllDivisions(); // ✅ gọi trực tiếp trong EmployeeDBContext
        int total = empDB.countEmployees(search, divisionId);
        int totalPages = (int) Math.ceil(total * 1.0 / pageSize);

        System.out.println("Employees loaded: " + employees.size());
        System.out.println("Divisions loaded: " + divisions.size());

        // --- Gửi sang JSP ---
        req.setAttribute("employees", employees);
        req.setAttribute("divisions", divisions);
        req.setAttribute("pageIndex", pageIndex);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("search", search);
        req.setAttribute("divisionId", divisionId);

        System.out.println("Forwarding to: /view/controller/dashboard.jsp");
        System.out.println("=== [DashboardController] processGet END ===");

        req.getRequestDispatcher("/view/controller/dashboard.jsp").forward(req, resp);
    }

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        System.out.println("=== [DashboardController] processPost START ===");

        String[] empIds = req.getParameterValues("empId");
        EmployeeDBContext empDB = new EmployeeDBContext();

        if (empIds != null) {
            System.out.println("Updating divisions for " + empIds.length + " employees...");
            for (String idStr : empIds) {
                try {
                    int empId = Integer.parseInt(idStr);
                    String rawDivId = req.getParameter("division_" + empId);
                    if (rawDivId != null && !rawDivId.isEmpty()) {
                        int divId = Integer.parseInt(rawDivId);
                        empDB.updateDivision(empId, divId);
                        System.out.println(" - Employee " + empId + " updated to division " + divId);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid empId or divId: " + idStr);
                }
            }
        } else {
            System.out.println("No employee IDs received from POST.");
        }

        System.out.println("Redirecting to: /controller/dashboard");
        System.out.println("=== [DashboardController] processPost END ===");

        resp.sendRedirect(req.getContextPath() + "/controller/dashboard");
    }
}