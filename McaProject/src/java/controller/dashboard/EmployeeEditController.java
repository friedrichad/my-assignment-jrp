package controller.dashboard;

import dal.EmployeeDBContext;
import model.Employee;
import controller.auth.BaseAuthorizationController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/controller/employee/edit")
public class EmployeeEditController extends BaseAuthorizationController {

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, model.self.User user)
            throws ServletException, IOException {

        String rawId = req.getParameter("id");
        if (rawId == null) {
            resp.sendRedirect(req.getContextPath() + "/controller/dashboard");
            return;
        }

        try {
            int id = Integer.parseInt(rawId);
            EmployeeDBContext db = new EmployeeDBContext();
            Employee emp = db.getEmployeeById(id);

            if (emp == null) {
                resp.sendRedirect(req.getContextPath() + "/controller/dashboard");
                return;
            }

            req.setAttribute("employee", emp);
            req.getRequestDispatcher("/view/controller/employee/edit.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/controller/dashboard");
        }
    }

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, model.self.User user)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String name = req.getParameter("name");
            boolean gender = "male".equals(req.getParameter("gender"));
            String email = req.getParameter("email");

            EmployeeDBContext db = new EmployeeDBContext();
            db.updateEmployeeInfo(id, name, gender, email);

            resp.sendRedirect(req.getContextPath() + "/controller/dashboard");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/controller/dashboard");
        }
    }
}
