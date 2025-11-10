package controller.dashboard;

import dal.EmployeeDBContext;
import model.Employee;
import controller.auth.BaseAuthorizationController;
import dal.UserDBContext;
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
            int eid = Integer.parseInt(req.getParameter("id"));
            EmployeeDBContext edb = new EmployeeDBContext();
            UserDBContext udb = new UserDBContext();

            // ✅ Lấy thông tin employee
            Employee employee = edb.getEmployeeById(eid);
            if (employee == null) {
                resp.getWriter().println("Không tìm thấy nhân viên.");
                return;
            }

            // ✅ Lấy trạng thái user (is_active)
            boolean isActive = udb.getUserStatusByEmployeeId(eid);
            employee.setActive(isActive);

            // ✅ Dữ liệu cho dropdown
            ArrayList<Division> divisions = edb.getAllDivisions();
            ArrayList<Employee> supervisors = edb.getEmployees(null, null, 1, 1000);

            req.setAttribute("employee", employee);
            req.setAttribute("divisions", divisions);
            req.setAttribute("supervisors", supervisors);

            req.setAttribute("contentPage", "/view/controller/employee/edit.jsp");
            req.getRequestDispatcher("/view/include/layout.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Lỗi khi tải trang chỉnh sửa: " + e.getMessage());
        }
    }

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        try {
            int eid = Integer.parseInt(req.getParameter("id"));
            String name = req.getParameter("name");
            boolean gender = Boolean.parseBoolean(req.getParameter("gender"));
            String email = req.getParameter("email");
            boolean isActive = req.getParameter("isActive") != null; // checkbox true/false

            int divisionId = Integer.parseInt(req.getParameter("divisionId"));
            String supParam = req.getParameter("supervisorId");
            Integer supervisorId = (supParam == null || supParam.isEmpty()) ? null : Integer.parseInt(supParam);

            EmployeeDBContext edb = new EmployeeDBContext();
            UserDBContext udb = new UserDBContext();

            // ✅ Kiểm tra nếu user của employee này là admin → không cho vô hiệu hóa
            boolean isAdmin = udb.isEmployeeAdmin(eid);
            if (isAdmin && !isActive) {
                Employee employee = edb.getEmployeeById(eid);
                ArrayList<Division> divisions = edb.getAllDivisions();
                ArrayList<Employee> supervisors = edb.getEmployees(null, null, 1, 1000);

                employee.setActive(true); // giữ nguyên
                req.setAttribute("employee", employee);
                req.setAttribute("divisions", divisions);
                req.setAttribute("supervisors", supervisors);
                req.setAttribute("error", "❌ Không thể vô hiệu hóa tài khoản Admin!");

                req.setAttribute("contentPage", "/view/controller/employee/edit.jsp");
                req.getRequestDispatcher("/view/include/layout.jsp").forward(req, resp);
                return;
            }

            // ✅ Cập nhật Employee info
            edb.updateEmployeeInfo(eid, name, gender, email);
            edb.updateDivision(eid, divisionId);
            edb.assignSupervisor(eid, supervisorId);

            // ✅ Cập nhật trạng thái user.is_active
            udb.updateUserStatusByEmployeeId(eid, isActive);

            resp.sendRedirect(req.getContextPath() + "/controller/dashboard?success=updated");

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Lỗi cập nhật nhân viên: " + e.getMessage());
        }
    }
}