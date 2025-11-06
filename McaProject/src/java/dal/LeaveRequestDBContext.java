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
            SELECT e.eid, e.ename, e.supervisorid
            FROM Employee e
            WHERE e.supervisorid = ?
            UNION ALL
            SELECT e2.eid, e2.ename, e2.supervisorid
            FROM Employee e2
            INNER JOIN RecursiveCTE r ON e2.supervisorid = r.eid
        )
        SELECT lr.*, e.ename AS employee_name, lt.typename AS leave_type
        FROM LeaveRequest lr
        INNER JOIN Employee e ON lr.eid = e.eid
        INNER JOIN LeaveType lt ON lr.typeid = lt.typeid
        WHERE lr.eid IN (SELECT eid FROM RecursiveCTE)
        ORDER BY lr.requested_at DESC
    """;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, supervisorId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
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
                lr.setLeaveTypeName(rs.getString("leave_type"));

                Employee emp = new Employee();
                emp.setId(rs.getInt("eid"));
                emp.setEmployeeName(rs.getString("employee_name"));
                lr.setEmployee(emp);

                list.add(lr);
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

    @Override
    public LeaveRequest get(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
