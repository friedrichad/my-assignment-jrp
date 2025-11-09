package controller.dashboard;

import dal.EmployeeDBContext;
import model.Employee;
import controller.auth.BaseAuthorizationController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import model.Division;
import model.self.User;

@WebServlet("/controller/employee/edit")
public class EmployeeEditController extends BaseAuthorizationController {

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            EmployeeDBContext db = new EmployeeDBContext();

            Employee employee = db.getEmployeeById(id);
            ArrayList<Division> divisions = db.getAllDivisions();
            ArrayList<Employee> supervisors = db.getEmployees(null, null, 1, 1000); // lấy danh sách sup

            req.setAttribute("employee", employee);
            req.setAttribute("divisions", divisions);
            req.setAttribute("supervisors", supervisors);

            req.getRequestDispatcher("/view/controller/employee/edit.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Lỗi tải trang chỉnh sửa: " + e.getMessage());
        }
    }

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String name = req.getParameter("name");
            boolean gender = Boolean.parseBoolean(req.getParameter("gender"));
            String email = req.getParameter("email");

            int divisionId = Integer.parseInt(req.getParameter("divisionId"));
            String supParam = req.getParameter("supervisorId");
            Integer supervisorId = (supParam == null || supParam.isEmpty()) ? null : Integer.parseInt(supParam);

            EmployeeDBContext db = new EmployeeDBContext();
            db.updateEmployeeInfo(id, name, gender, email);
            db.updateDivision(id, divisionId);
            db.assignSupervisor(id, supervisorId);

            resp.sendRedirect(req.getContextPath() + "/controller/dashboard?success=updated");

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Lỗi cập nhật nhân viên: " + e.getMessage());
        }
    }
}