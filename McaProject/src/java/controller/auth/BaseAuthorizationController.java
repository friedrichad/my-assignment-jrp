/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.auth;

import dal.RoleDBContext;
import model.self.Role;
import model.self.Feature;
import model.self.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 *
 * @author Hiro
 */
public abstract class BaseAuthorizationController extends BaseAuthenticationController {

    private boolean isAuthorized(HttpServletRequest req, User user) {
    String url = req.getServletPath();
    System.out.println("[DEBUG] isAuthorized() CALLED for URL: '" + url + "'");

    if (user.getRoles() == null || user.getRoles().isEmpty()) {
        System.out.println("[DEBUG] Roles chưa được load, đang fetch từ DB...");
        RoleDBContext db = new RoleDBContext();
        ArrayList<Role> roles = db.getByUserId(user.getId());
        System.out.println("[DEBUG] Số lượng roles load được: " + (roles != null ? roles.size() : "null"));
        user.setRoles(roles);
        req.getSession().setAttribute("auth", user); // cập nhật lại session
    }

    if (user.getRoles() == null) {
        System.out.println("[DEBUG] user.getRoles() vẫn NULL sau khi fetch!");
        return false;
    }

    for (Role role : user.getRoles()) {
        if (role == null) {
            System.out.println("[DEBUG] Role object is NULL!");
            continue;
        }
        System.out.println("[DEBUG] Checking role: " + role.getRoleName());

        if (role.getFeatures() == null) {
            System.out.println("[DEBUG] role.getFeatures() is NULL for role: " + role.getRoleName());
            continue;
        }

        for (Feature feature : role.getFeatures()) {
            if (feature == null || feature.getfUrl() == null) continue;
            
            String featureUrl = feature.getfUrl().trim();
            String requestUrl = url.trim();

            System.out.println("[DEBUG] Comparing: '" + featureUrl + "' vs '" + requestUrl + "'");

            if (featureUrl.equalsIgnoreCase(requestUrl)) {
                System.out.println("[DEBUG] >>> MATCH FOUND!");
                return true;
            }
        }
    }

    System.out.println("[DEBUG] No match found for URL: '" + url + "'");
    return false;
}
    protected abstract void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException;

    protected abstract void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        if (isAuthorized(req, user)) {
            processPost(req, resp, user);
        } else {
            resp.getWriter().println("Access denied!");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        System.out.println(">>> [DEBUG] BaseAuthorizationController.doGet() called with user: " + user.getAccount());
        if (isAuthorized(req, user)) {
            System.out.println(">>> [DEBUG] isAuthorized() entered for userId = " + user.getId());
            processGet(req, resp, user);
        } else {
            resp.getWriter().println("Access denied!");
        }
    }
}
