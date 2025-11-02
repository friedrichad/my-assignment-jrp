/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.*;
import model.self.User;
import java.util.ArrayList;
import model.self.Role;
import model.Employee;

public class UserDBContext extends DBContext<User> {

    public User getUserByLogin(String account, String password) {
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            String sql = """
    SELECT u.uid, u.disname, u.account, u.password, u.is_active,
           r.rid, r.rolename,
           e.eid, e.ename
    FROM [User] u
    LEFT JOIN Matching m ON u.uid = m.uid
    LEFT JOIN Employee e ON m.eid = e.eid
    LEFT JOIN UserRole ur ON u.uid = ur.uid
    LEFT JOIN Role r ON ur.rid = r.rid
    WHERE u.account = ? AND u.password = ? AND u.is_active = 1
""";

            stm = connection.prepareStatement(sql);
            stm.setString(1, account);
            stm.setString(2, password);
            rs = stm.executeQuery();

            User u = null;
            while (rs.next()) {
                if (u == null) {
                    u = new User();
                    u.setId(rs.getInt("uid"));
                    u.setDisname(rs.getString("disname"));
                    u.setAccount(rs.getString("account"));
                    u.setPassword(rs.getString("password"));
                    u.setIsActive(rs.getBoolean("is_active"));
                    u.setRoles(new ArrayList<>());

                    // ✅ Gán Employee nếu có
                    int eid = rs.getInt("eid");
                    if (!rs.wasNull()) {
                        Employee e = new Employee();
                        e.setId(eid);
                        e.setEmployeeName(rs.getString("ename"));
                        u.setEmployee(e);
                    } else {
                        u.setEmployee(null);
                    }
                }

                // ✅ Gán Role nếu có
                int rid = rs.getInt("rid");
                String rolename = rs.getString("rolename");
                if (rolename != null) {
                    Role r = new Role();
                    r.setId(rid);
                    r.setRoleName(rolename);
                    u.getRoles().add(r);
                }
            }

            // Debug: kiểm tra xem có employee không
            if (u != null && u.getEmployee() == null) {
                System.out.println("⚠️ User " + u.getAccount() + " không có Employee gắn kèm!");
            }

            return u;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeConnection();
        }

        return null;
    }

    @Override
    public ArrayList<User> list() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public User get(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void insert(User model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(User model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(User model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
