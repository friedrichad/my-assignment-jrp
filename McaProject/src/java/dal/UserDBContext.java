/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.*;
import model.self.User;
import java.util.ArrayList;
import model.Division;
import model.self.Role;
import model.Employee;

public class UserDBContext extends DBContext<User> {

    public User getUserByLogin(String account, String password) {
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            String sql = """
            SELECT u.uid, u.disname, u.account, u.password, u.is_active,
                   e.eid, e.ename
            FROM [User] u
            LEFT JOIN Matching m ON u.uid = m.uid
            LEFT JOIN Employee e ON m.eid = e.eid
            WHERE u.account = ? AND u.password = ? AND u.is_active = 1
        """;

            stm = connection.prepareStatement(sql);
            stm.setString(1, account);
            stm.setString(2, password);
            rs = stm.executeQuery();

            User u = null;
            if (rs.next()) {
                u = new User();
                u.setId(rs.getInt("uid"));
                u.setDisname(rs.getString("disname"));
                u.setAccount(rs.getString("account"));
                u.setPassword(rs.getString("password"));
                u.setIsActive(rs.getBoolean("is_active"));
                u.setRoles(null); // ⚠️ để null, để controller tự fetch sau

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

            return u;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stm != null) {
                    stm.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public ArrayList<Employee> getAllSubordinates(int supervisorId) {
        ArrayList<Employee> list = new ArrayList<>();
        String sql = """
        WITH RecursiveCTE AS (
            SELECT e.eid, e.ename, e.supervisorid
            FROM Employee e
            WHERE e.supervisorid = ?
            UNION ALL
            SELECT e2.eid, e2.ename, e2.supervisorid
            FROM Employee e2
            INNER JOIN RecursiveCTE r ON e2.supervisorid = r.eid
        )
        SELECT * FROM RecursiveCTE
    """;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, supervisorId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Employee e = new Employee();
                e.setId(rs.getInt("eid"));
                e.setEmployeeName(rs.getString("ename"));
                list.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public ArrayList<User> getAllUsers() {
    ArrayList<User> list = new ArrayList<>();
    String sql = """
        SELECT u.uid, u.disname, u.account, u.is_active,
               r.rid, r.rolename,
               e.eid, e.ename, e.supervisorid
        FROM [User] u
        LEFT JOIN UserRole ur ON u.uid = ur.uid
        LEFT JOIN Role r ON ur.rid = r.rid
        LEFT JOIN Employee e ON u.uid = e.eid
        ORDER BY u.uid
    """;

    try (PreparedStatement stm = connection.prepareStatement(sql);
         ResultSet rs = stm.executeQuery()) {
        while (rs.next()) {
            User u = new User();
            u.setId(rs.getInt("uid"));
            u.setDisname(rs.getString("disname"));
            u.setAccount(rs.getString("account"));
            u.setIsActive(rs.getBoolean("is_active"));

            // Role
            int rid = rs.getInt("rid");
            if (!rs.wasNull()) {
                Role role = new Role();
                role.setId(rid);
                role.setRoleName(rs.getString("rolename"));
                ArrayList<Role> roles = new ArrayList<>();
                roles.add(role);
                u.setRoles(roles);
            } else {
                u.setRoles(new ArrayList<>()); // hoặc null tùy bạn
            }

            // Employee
            int eid = rs.getInt("eid");
            if (!rs.wasNull()) {
                Employee e = new Employee();
                e.setId(eid);
                e.setEmployeeName(rs.getString("ename"));

                // Supervisor
                int supId = rs.getInt("supervisorid");
                if (!rs.wasNull()) {
                    Employee supervisor = new Employee();
                    supervisor.setId(supId);
                    e.setSupervisor(supervisor);
                } else {
                    e.setSupervisor(null);
                }

                u.setEmployee(e);
            } else {
                u.setEmployee(null);
            }

            list.add(u);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    return list;
}
    public ArrayList<Employee> getTopEmployees() {
    ArrayList<Employee> list = new ArrayList<>();
    String sql = "SELECT eid, ename FROM Employee WHERE supervisorid IS NULL";

    try (PreparedStatement stm = connection.prepareStatement(sql);
         ResultSet rs = stm.executeQuery()) {
        while (rs.next()) {
            Employee e = new Employee();
            e.setId(rs.getInt("eid"));
            e.setEmployeeName(rs.getString("ename"));
            list.add(e);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return list;
}
    public void assignSupervisor(int empId, Integer supId) {
    String sql = "UPDATE Employee SET supervisorid = ? WHERE eid = ?";
    try (PreparedStatement stm = connection.prepareStatement(sql)) {
        if (supId != null) {
            stm.setInt(1, supId);
        } else {
            stm.setNull(1, java.sql.Types.INTEGER);
        }
        stm.setInt(2, empId);
        stm.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public ArrayList<Division> getAllDivisions() {
    ArrayList<Division> list = new ArrayList<>();
    String sql = "SELECT divid, divname FROM Division"; // đổi tên cột cho đúng DB
    try (PreparedStatement stm = connection.prepareStatement(sql);
         ResultSet rs = stm.executeQuery()) {
        while (rs.next()) {
            Division d = new Division();
            d.setId(rs.getInt("divid"));
            d.setDivisionName(rs.getString("divname"));
            list.add(d);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return list;
}

public void updateDivision(int empId, String divisionName) {
    String sql = """
        UPDATE Employee
        SET divisionid = (SELECT divid FROM Division WHERE divname = ?)
        WHERE eid = ?
    """;
    try (PreparedStatement stm = connection.prepareStatement(sql)) {
        stm.setString(1, divisionName);
        stm.setInt(2, empId);
        stm.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
public boolean changePassword(int userId, String currentPass, String newPass) {
    try {
        String sqlCheck = "SELECT password FROM [User] WHERE uid = ?";
        PreparedStatement stmCheck = connection.prepareStatement(sqlCheck);
        stmCheck.setInt(1, userId);
        ResultSet rs = stmCheck.executeQuery();

        if (rs.next()) {
            String storedPass = rs.getString("password");
            if (!storedPass.equals(currentPass)) { // sau này nên hash password nhé
                return false;
            }
        } else {
            return false;
        }

        // ⚠️ Sửa chỗ này
        String sqlUpdate = "UPDATE [User] SET password = ? WHERE uid = ?";
        PreparedStatement stmUpdate = connection.prepareStatement(sqlUpdate);
        stmUpdate.setString(1, newPass);
        stmUpdate.setInt(2, userId);
        stmUpdate.executeUpdate();

        return true;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
public boolean deactivateAccount(int userId) {
    String sql = "UPDATE [User] SET is_active = 0 WHERE uid = ?";
    try (PreparedStatement stm = connection.prepareStatement(sql)) {
        stm.setInt(1, userId);
        int rows = stm.executeUpdate();
        return rows > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

 public void updateUserStatusByEmployeeId(int employeeId, boolean isActive) {
    String sql = """
        UPDATE u
        SET u.is_active = ?
        FROM [User] u
        INNER JOIN Matching m ON u.uid = m.uid
        WHERE m.eid = ?
    """;
    try (PreparedStatement stm = connection.prepareStatement(sql)) {
        stm.setBoolean(1, isActive);
        stm.setInt(2, employeeId);
        stm.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    /** Kiểm tra xem User có phải Admin không (tránh vô hiệu hóa Admin) **/
    public boolean isAdminByEmployeeId(int employeeId) {
        String sql = """
            SELECT COUNT(*) AS count
            FROM [User] u
            JOIN Matching m ON u.uid = m.uid
            JOIN UserRole ur ON u.uid = ur.uid
            JOIN Role r ON ur.rid = r.rid
            WHERE m.eid = ? AND r.rolename = 'Admin'
        """;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, employeeId);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
   public boolean getUserStatusByEmployeeId(int employeeId) {
    String sql = """
        SELECT u.is_active
        FROM [User] u
        INNER JOIN Matching m ON u.uid = m.uid
        WHERE m.eid = ?
    """;
    try (PreparedStatement stm = connection.prepareStatement(sql)) {
        stm.setInt(1, employeeId);
        try (ResultSet rs = stm.executeQuery()) {
            if (rs.next()) {
                return rs.getBoolean("is_active");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    // Không tìm thấy mapping -> trả false (hoặc true tùy policy; mình chọn false an toàn)
    return false;
}

public boolean isEmployeeAdmin(int employeeId) {
    String sql = """
        SELECT COUNT(*) AS total
        FROM [User] u
        INNER JOIN Matching m ON u.uid = m.uid
        INNER JOIN UserRole ur ON u.uid = ur.uid
        INNER JOIN Role r ON ur.rid = r.rid
        WHERE m.eid = ? AND LOWER(r.rolename) = 'admin'
    """;
    try (PreparedStatement stm = connection.prepareStatement(sql)) {
        stm.setInt(1, employeeId);
        try (ResultSet rs = stm.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
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
