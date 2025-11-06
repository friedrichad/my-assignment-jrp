package dal;

import java.sql.*;
import java.util.ArrayList;
import model.Division;
import model.Employee;

public class EmployeeDBContext extends DBContext<Employee> {

    // ==========================================================
    // 📌 1️⃣ Lấy danh sách nhân viên (tìm kiếm, lọc division, phân trang)
    // ==========================================================
    public ArrayList<Employee> getEmployees(String search, Integer divisionId, int pageIndex, int pageSize) {
        ArrayList<Employee> list = new ArrayList<>();
        try {
            String sql = """
                SELECT e.eid, e.ename, e.gender, e.dob, e.email,
                       e.supervisorid, s.ename AS supervisorName,
                       d.divid, d.divname
                FROM Employee e
                LEFT JOIN Employee s ON e.supervisorid = s.eid
                LEFT JOIN Division d ON e.divid = d.divid
                WHERE 1=1
            """;

            // --- Bộ lọc tìm kiếm ---
            if (search != null && !search.trim().isEmpty()) {
                sql += " AND e.ename LIKE ? ";
            }

            // --- Bộ lọc Division ---
            if (divisionId != null && divisionId > 0) {
                sql += " AND e.divid = ? ";
            }

            // --- Phân trang ---
            sql += " ORDER BY e.ename ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

            PreparedStatement stm = connection.prepareStatement(sql);

            int paramIndex = 1;
            if (search != null && !search.trim().isEmpty()) {
                stm.setString(paramIndex++, "%" + search + "%");
            }
            if (divisionId != null && divisionId > 0) {
                stm.setInt(paramIndex++, divisionId);
            }

            int offset = (pageIndex - 1) * pageSize;
            stm.setInt(paramIndex++, offset);
            stm.setInt(paramIndex++, pageSize);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Employee e = new Employee();
                e.setId(rs.getInt("eid"));
                e.setEmployeeName(rs.getString("ename"));
                e.setGender(rs.getBoolean("gender"));
                e.setDob(rs.getDate("dob"));
                e.setEmail(rs.getString("email"));

                // Supervisor
                Employee supervisor = new Employee();
                supervisor.setId(rs.getInt("supervisorid"));
                supervisor.setEmployeeName(rs.getString("supervisorName"));
                e.setSupervisor(supervisor);

                // Division
                Division d = new Division();
                d.setId(rs.getInt("divid"));
                d.setDivisionName(rs.getString("divname"));
                e.setDivision(d);

                list.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    // ==========================================================
    // 📌 2️⃣ Đếm tổng số nhân viên (để tính tổng trang)
    // ==========================================================
    public int countEmployees(String search, Integer divisionId) {
        int total = 0;
        try {
            String sql = "SELECT COUNT(*) AS total FROM Employee e WHERE 1=1 ";

            if (search != null && !search.trim().isEmpty()) {
                sql += " AND e.ename LIKE ? ";
            }
            if (divisionId != null && divisionId > 0) {
                sql += " AND e.divid = ? ";
            }

            PreparedStatement stm = connection.prepareStatement(sql);
            int paramIndex = 1;
            if (search != null && !search.trim().isEmpty()) {
                stm.setString(paramIndex++, "%" + search + "%");
            }
            if (divisionId != null && divisionId > 0) {
                stm.setInt(paramIndex++, divisionId);
            }

            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return total;
    }

    // ==========================================================
    // 📌 3️⃣ Lấy tất cả division
    // ==========================================================
    public ArrayList<Division> getAllDivisions() {
        ArrayList<Division> list = new ArrayList<>();
        String sql = "SELECT divid, divname FROM Division";
        try (PreparedStatement stm = connection.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {
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

    // ==========================================================
    // 📌 4️⃣ Cập nhật Division cho nhân viên
    // ==========================================================
    public void updateDivision(int empId, String divisionName) {
        String sql = """
            UPDATE Employee
            SET divid = (SELECT divid FROM Division WHERE divname = ?)
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

    // ==========================================================
    // 📌 5️⃣ Gán supervisor cho nhân viên
    // ==========================================================
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
    // Lấy danh sách Division (phục vụ dropdown lọc)


// Cập nhật division cho employee
    public void updateDivision(int empId, int divId) {
        try {
            String sql = "UPDATE Employee SET divid = ? WHERE eid = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, divId);
            stm.setInt(2, empId);
            stm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public Employee getEmployeeById(int id) {
    Employee e = null;
    try {
        String sql = "SELECT e.eid, e.ename, e.gender, e.dob, e.email, "
                   + "e.supervisorid, s.ename AS supervisorName, "
                   + "d.divid, d.divname "
                   + "FROM Employee e "
                   + "LEFT JOIN Employee s ON e.supervisorid = s.eid "
                   + "LEFT JOIN Division d ON e.divid = d.divid "
                   + "WHERE e.eid = ?";
        PreparedStatement stm = connection.prepareStatement(sql);
        stm.setInt(1, id);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            e = new Employee();
            e.setId(rs.getInt("eid"));
            e.setEmployeeName(rs.getString("ename"));
            e.setGender(rs.getBoolean("gender"));
            e.setDob(rs.getDate("dob"));
            e.setEmail(rs.getString("email"));

            Employee sup = new Employee();
            sup.setId(rs.getInt("supervisorid"));
            sup.setEmployeeName(rs.getString("supervisorName"));
            e.setSupervisor(sup);

            Division d = new Division();
            d.setId(rs.getInt("divid"));
            d.setDivisionName(rs.getString("divname"));
            e.setDivision(d);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return e;
}

// Cập nhật thông tin nhân viên
public void updateEmployeeInfo(int id, String name, boolean gender, String email) {
    try {
        String sql = "UPDATE Employee SET ename = ?, gender = ?, email = ? WHERE eid = ?";
        PreparedStatement stm = connection.prepareStatement(sql);
        stm.setString(1, name);
        stm.setBoolean(2, gender);
        stm.setString(3, email);
        stm.setInt(4, id);
        stm.executeUpdate();
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

    // ==========================================================
    // ❌ Override abstract methods (chưa dùng)
    // ==========================================================
    @Override
    public ArrayList<Employee> list() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Employee get(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void insert(Employee model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(Employee model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Employee model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
