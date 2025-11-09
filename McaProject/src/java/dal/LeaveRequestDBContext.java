/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import model.LeaveRequest;
import java.sql.*;
import java.util.ArrayList;
import model.BaseObject;
import model.LeaveRequest;
import model.Employee;
import model.LeaveType;

public class LeaveRequestDBContext extends DBContext<LeaveRequest> {

    public void insert(LeaveRequest lr) {
        try {
            // Bắt đầu transaction
            connection.setAutoCommit(false);

            String sql = """
            INSERT INTO LeaveRequest
                (eid, typeid, start_date, end_date, num_days, reason, status, approverid, requested_at)
            VALUES (?, ?, ?, ?, ?, ?, DEFAULT, NULL, DEFAULT)
        """;

            PreparedStatement stm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stm.setInt(1, lr.getEid());
            stm.setInt(2, lr.getTypeid());
            stm.setDate(3, new java.sql.Date(lr.getStartDate().getTime()));
            stm.setDate(4, new java.sql.Date(lr.getEndDate().getTime()));
            stm.setDouble(5, lr.getNumDays());
            stm.setString(6, lr.getReason());

            stm.executeUpdate();

            // Lấy ID của bản ghi vừa insert
            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next()) {
                lr.setId(rs.getInt(1));
            }

            // Commit transaction
            connection.commit();
            System.out.println("✅ Insert thành công đơn nghỉ phép (reqid=" + lr.getId() + ", eid=" + lr.getEid() + ")");
        } catch (SQLException e) {
            try {
                // Nếu lỗi thì rollback
                connection.rollback();
                System.err.println("⚠️ Rollback do lỗi SQL: " + e.getMessage());
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } finally {
            try {
                // Trả lại chế độ auto commit cho kết nối
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<LeaveRequest> getRequestsByUser(int eid) {
        ArrayList<LeaveRequest> list = new ArrayList<>();
        String sql = """
        SELECT lr.reqid, lr.eid, e.ename AS employee_name,
               lr.typeid, lt.typename AS leave_type,
               lr.start_date, lr.end_date, lr.num_days,
               lr.reason, lr.status, lr.approverid, lr.requested_at
        FROM LeaveRequest lr
        INNER JOIN Employee e ON lr.eid = e.eid
        INNER JOIN LeaveType lt ON lr.typeid = lt.typeid
        WHERE lr.eid = ?
        ORDER BY lr.requested_at DESC
    """;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, eid); //  set parameter trước
            try (ResultSet rs = stm.executeQuery()) { //  rồi mới query
                while (rs.next()) {
                    LeaveRequest lr = new LeaveRequest();
                    lr.setId(rs.getInt("reqid"));
                    lr.setEid(rs.getInt("eid"));
                    lr.setTypeid(rs.getInt("typeid"));
                    lr.setLeaveTypeName(rs.getString("leave_type"));
                    lr.setStartDate(rs.getDate("start_date"));
                    lr.setEndDate(rs.getDate("end_date"));
                    lr.setNumDays(rs.getDouble("num_days"));
                    lr.setReason(rs.getString("reason"));
                    lr.setStatus(rs.getString("status"));
                    lr.setApproverid((Integer) rs.getObject("approverid"));
                    lr.setRequestedAt(rs.getTimestamp("requested_at"));

                    Employee emp = new Employee();
                    emp.setId(rs.getInt("eid"));
                    emp.setEmployeeName(rs.getString("employee_name"));
                    lr.setEmployee(emp);

                    list.add(lr);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }

    public int getEidByUid(int uid) {
        String sql = """
        SELECT m.eid 
        FROM [User] u
        INNER JOIN Matching m ON u.uid = m.uid
        WHERE u.uid = ?
    """;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, uid);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt("eid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Không tìm thấy
    }

    public LeaveRequest getById(int reqid) {
        String sql = """
        SELECT lr.reqid, lr.eid, e.ename AS employee_name,
               lr.typeid, lt.typename AS leave_type,
               lr.start_date, lr.end_date, lr.num_days,
               lr.reason, lr.status, lr.approverid, lr.requested_at
        FROM LeaveRequest lr
        INNER JOIN Employee e ON lr.eid = e.eid
        INNER JOIN LeaveType lt ON lr.typeid = lt.typeid
        WHERE lr.reqid = ?
    """;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, reqid);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    LeaveRequest lr = new LeaveRequest();
                    lr.setId(rs.getInt("reqid"));
                    lr.setEid(rs.getInt("eid"));
                    lr.setTypeid(rs.getInt("typeid"));
                    lr.setStartDate(rs.getDate("start_date"));
                    lr.setEndDate(rs.getDate("end_date"));
                    lr.setNumDays(rs.getDouble("num_days"));
                    lr.setReason(rs.getString("reason"));
                    lr.setStatus(rs.getString("status"));
                    lr.setApproverid((Integer) rs.getObject("approverid"));
                    lr.setRequestedAt(rs.getTimestamp("requested_at"));
                    lr.setLeaveTypeName(rs.getString("leave_type")); // thêm tên loại nghỉ vào request

                    // Gắn thông tin nhân viên
                    Employee emp = new Employee();
                    emp.setId(rs.getInt("eid"));
                    emp.setEmployeeName(rs.getString("employee_name"));
                    lr.setEmployee(emp);

                    return lr;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }

    public void updatePendingRequest(LeaveRequest lr) {
        String sql = """
        UPDATE LeaveRequest
        SET start_date = ?, end_date = ?, reason = ?
        WHERE reqid = ? AND status = 'Pending'
    """;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setDate(1, new java.sql.Date(lr.getStartDate().getTime()));
            stm.setDate(2, new java.sql.Date(lr.getEndDate().getTime()));
            stm.setString(3, lr.getReason());
            stm.setInt(4, lr.getId());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<LeaveType> listLeaveType() {
        ArrayList<LeaveType> list = new ArrayList<>();
        String sql = "SELECT typeid, typename FROM LeaveType";
        try (PreparedStatement stm = connection.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                LeaveType lt = new LeaveType();
                lt.setId(rs.getInt("typeid"));
                lt.setTypename(rs.getString("typename"));
                list.add(lt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

// Lấy danh sách đơn nghỉ của toàn bộ cấp dưới
    public ArrayList<LeaveRequest> getRequestsOfSubordinates(int supervisorId) {
        ArrayList<LeaveRequest> list = new ArrayList<>();
        String sql = """
        WITH RecursiveCTE AS (
            SELECT e.eid
            FROM Employee e
            WHERE e.supervisorid = ?
            UNION ALL
            SELECT e2.eid
            FROM Employee e2
            INNER JOIN RecursiveCTE r ON e2.supervisorid = r.eid
        )
        SELECT lr.reqid, lr.eid, e.ename AS employee_name,
               lr.typeid, lt.typename AS leave_type,
               lr.start_date, lr.end_date, lr.num_days,
               lr.reason, lr.status, lr.approverid, lr.requested_at
        FROM LeaveRequest lr
        INNER JOIN Employee e ON lr.eid = e.eid
        INNER JOIN LeaveType lt ON lr.typeid = lt.typeid
        WHERE lr.eid IN (SELECT eid FROM RecursiveCTE)
        ORDER BY lr.requested_at DESC
    """;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, supervisorId);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    LeaveRequest lr = new LeaveRequest();
                    lr.setId(rs.getInt("reqid"));
                    lr.setEid(rs.getInt("eid"));
                    lr.setTypeid(rs.getInt("typeid"));
                    lr.setLeaveTypeName(rs.getString("leave_type"));
                    lr.setStartDate(rs.getDate("start_date"));
                    lr.setEndDate(rs.getDate("end_date"));
                    lr.setNumDays(rs.getDouble("num_days"));
                    lr.setReason(rs.getString("reason"));
                    lr.setStatus(rs.getString("status"));
                    lr.setApproverid((Integer) rs.getObject("approverid"));
                    lr.setRequestedAt(rs.getTimestamp("requested_at"));

                    Employee emp = new Employee();
                    emp.setId(rs.getInt("eid"));
                    emp.setEmployeeName(rs.getString("employee_name"));
                    lr.setEmployee(emp);

                    list.add(lr);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

// Cập nhật trạng thái (duyệt hoặc từ chối)
    public void reviewLeaveRequest(int reqid, int approverId, String status, String reason) {
        String sql = """
        UPDATE LeaveRequest
        SET status = ?, approverid = ?, reason = ?
        WHERE reqid = ? AND status = 'Pending'
    """;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, status);
            stm.setInt(2, approverId);
            stm.setString(3, reason);
            stm.setInt(4, reqid);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkOverlapLeave(int eid, Date start, Date end) {
        String sql = """
        SELECT COUNT(*) AS cnt FROM LeaveRequest
                WHERE eid = ? 
                AND (
                    (start_date <= ? AND end_date >= ?) OR
                    (start_date <= ? AND end_date >= ?) OR
                    (start_date >= ? AND end_date <= ?)
    """;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, eid);
            stm.setDate(2, start);
            stm.setDate(3, start);
            stm.setDate(4, end);
            stm.setDate(5, end);
            stm.setDate(6, start);
            stm.setDate(7, end);

            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt("cnt") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getTotalApprovedDays(int eid) {
        int total = 0;
        String sql = "SELECT SUM(num_days) FROM LeaveRequest WHERE eid = ? AND status = 'Approved'";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, eid);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
            rs.close();
            stm.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    public ArrayList<LeaveRequest> searchRequests(int eid, String status, Date from, Date to, int page, int size) {
        ArrayList<LeaveRequest> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM LeaveRequest WHERE eid = ?");
        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = ?");
        }
        if (from != null) {
            sql.append(" AND start_date >= ?");
        }
        if (to != null) {
            sql.append(" AND end_date <= ?");
        }
        sql.append(" ORDER BY requested_at DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement stm = connection.prepareStatement(sql.toString())) {
            int i = 1;
            stm.setInt(i++, eid);
            if (status != null && !status.isEmpty()) {
                stm.setString(i++, status);
            }
            if (from != null) {
                stm.setDate(i++, from);
            }
            if (to != null) {
                stm.setDate(i++, to);
            }
            stm.setInt(i++, (page - 1) * size);
            stm.setInt(i++, size);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                LeaveRequest lr = new LeaveRequest();
                lr.setId(rs.getInt("reqid"));
                lr.setStartDate(rs.getDate("start_date"));
                lr.setEndDate(rs.getDate("end_date"));
                lr.setReason(rs.getString("reason"));
                lr.setStatus(rs.getString("status"));
                lr.setNumDays(rs.getDouble("num_days"));
                lr.setRequestedAt(rs.getTimestamp("requested_at"));
                list.add(lr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

// Đếm tổng số đơn nghỉ
    public int countRequests(int eid, String status, Date from, Date to) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM LeaveRequest WHERE eid = ?");
        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = ?");
        }
        if (from != null) {
            sql.append(" AND start_date >= ?");
        }
        if (to != null) {
            sql.append(" AND end_date <= ?");
        }

        try (PreparedStatement stm = connection.prepareStatement(sql.toString())) {
            int i = 1;
            stm.setInt(i++, eid);
            if (status != null && !status.isEmpty()) {
                stm.setString(i++, status);
            }
            if (from != null) {
                stm.setDate(i++, from);
            }
            if (to != null) {
                stm.setDate(i++, to);
            }
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ArrayList<LeaveType> getLeaveTypes() {
        ArrayList<LeaveType> list = new ArrayList<>();
        try {
            String sql = "SELECT typeid, typename FROM LeaveType";
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                LeaveType t = new LeaveType();
                t.setId(rs.getInt("typeid"));
                t.setTypename(rs.getString("typename"));
                list.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<LeaveRequest> getRequestsInRange(int supervisorId, Date from, Date to) {
        ArrayList<LeaveRequest> list = new ArrayList<>();
        String sql = """
        SELECT e.eid, e.ename, lr.start_date, lr.end_date
        FROM Employee e
        LEFT JOIN LeaveRequest lr ON e.eid = lr.eid
            AND lr.status = 'Approved'
            AND lr.start_date <= ? AND lr.end_date >= ?
        WHERE (e.supervisorid = ? OR e.eid = ?)
        ORDER BY e.ename
    """;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setDate(1, to);
            stm.setDate(2, from);
            stm.setInt(3, supervisorId);
            stm.setInt(4, supervisorId); // để nhân viên tự xem chính mình
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                LeaveRequest lr = new LeaveRequest();
                Employee e = new Employee();
                e.setId(rs.getInt("eid"));
                e.setEmployeeName(rs.getString("ename"));
                lr.setEmployee(e);
                lr.setStartDate(rs.getDate("start_date"));
                lr.setEndDate(rs.getDate("end_date"));
                list.add(lr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<LeaveRequest> getApprovedLeavesByEmployee(int employeeId, Date start, Date end) {
        ArrayList<LeaveRequest> list = new ArrayList<>();
        String sql = """
        SELECT lr.reqid, lr.eid, lr.start_date, lr.end_date, lr.status, lt.typename AS leaveType
                            FROM LeaveRequest lr
                            INNER JOIN LeaveType lt ON lr.typeid = lt.typeid
                            WHERE lr.eid = ? AND lr.status = 'approved'
                              AND lr.start_date <= ? AND lr.end_date >= ?
    """;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, employeeId);
            stm.setDate(2, end);
            stm.setDate(3, start);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                LeaveRequest lr = new LeaveRequest();
                lr.setId(rs.getInt("reqid"));
                lr.setEid(rs.getInt("eid"));
                lr.setStartDate(rs.getDate("start_date"));
                lr.setEndDate(rs.getDate("end_date"));
                lr.setStatus(rs.getString("status"));
                lr.setLeaveTypeName(rs.getString("leaveType"));
                list.add(lr);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public ArrayList<LeaveRequest> getApprovedLeaves(Date from, Date to) {
        ArrayList<LeaveRequest> list = new ArrayList<>();
        String sql = """
        SELECT 
            lr.reqid,
            lr.start_date,
            lr.end_date,
            lr.status,
            lt.typename AS leaveTypeName,
            e.eid,
            e.ename,
            e.supervisorid,
            s.ename AS supervisorName
        FROM LeaveRequest lr
        JOIN LeaveType lt ON lt.typeid = lr.typeid
        JOIN Employee e ON e.eid = lr.eid
        LEFT JOIN Employee s ON s.eid = e.supervisorid
        WHERE lr.status = 'Approved'
          AND lr.start_date <= ?
          AND lr.end_date >= ?
    """;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setDate(1, to);
            stm.setDate(2, from);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                LeaveRequest lr = new LeaveRequest();
                lr.setId(rs.getInt("reqid"));
                lr.setStartDate(rs.getDate("start_date"));
                lr.setEndDate(rs.getDate("end_date"));
                lr.setStatus(rs.getString("status"));
                lr.setLeaveTypeName(rs.getString("leaveTypeName"));

                // Nhân viên nghỉ
                Employee emp = new Employee();
                emp.setId(rs.getInt("eid"));
                emp.setEmployeeName(rs.getString("ename"));

                // Người giám sát (supervisor)
                Employee supervisor = new Employee();
                supervisor.setEmployeeName(rs.getString("supervisorName"));
                emp.setSupervisor(supervisor);

                lr.setEmployee(emp);
                list.add(lr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public LeaveRequest get(int id) {
        LeaveRequest lr = null;
        String sql = "DELETE FROM LeaveRequest WHERE reqid = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                lr = new LeaveRequest();
                lr.setId(rs.getInt("id"));
                lr.setEid(rs.getInt("eid"));
                lr.setTypeid(rs.getInt("typeid"));
                lr.setStartDate(rs.getDate("startDate"));
                lr.setEndDate(rs.getDate("endDate"));
                lr.setNumDays(rs.getDouble("numDays"));
                lr.setStatus(rs.getString("status"));
                lr.setReason(rs.getString("reason"));
                lr.setRequestedAt(rs.getDate("requestedAt"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lr;
    }

    public void delete(int id) {
        String sql = "DELETE FROM LeaveRequest WHERE reqid = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            stm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<LeaveRequest> listByUser(int uid) {
        ArrayList<LeaveRequest> list = new ArrayList<>();
        String sql = """
        SELECT lr.reqid, lr.eid, lr.typeid, lr.start_date, lr.end_date,
                       lr.num_days, lr.status, lr.reason, lr.requested_at
                FROM LeaveRequest lr
                INNER JOIN Matching m ON lr.eid = m.eid
                WHERE m.uid = ?
                ORDER BY lr.requested_at DESC
    """;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, uid);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                LeaveRequest lr = new LeaveRequest();
                lr.setId(rs.getInt("id"));
                lr.setEid(rs.getInt("eid"));
                lr.setTypeid(rs.getInt("typeid"));
                lr.setStartDate(rs.getDate("startDate"));
                lr.setEndDate(rs.getDate("endDate"));
                lr.setNumDays(rs.getDouble("numDays"));
                lr.setStatus(rs.getString("status"));
                lr.setReason(rs.getString("reason"));
                lr.setRequestedAt(rs.getDate("requestedAt"));
                list.add(lr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<LeaveRequest> getRequestsByEmployeeId(int empId) {
        return getRequestsByUser(empId);
    }

    @Override
    public void update(LeaveRequest model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(LeaveRequest model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<LeaveRequest> list() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
