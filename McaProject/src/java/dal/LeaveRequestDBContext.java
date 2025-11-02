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

public class LeaveRequestDBContext extends DBContext<BaseObject> {

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
            lr.setReqid(rs.getInt(1));
        }

        // Commit transaction
        connection.commit();
        System.out.println("✅ Insert thành công đơn nghỉ phép (reqid=" + lr.getReqid() + ", eid=" + lr.getEid() + ")");
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
        closeConnection(); //  Đóng kết nối khi xong
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
                    lr.setReqid(rs.getInt("reqid"));
                    lr.setEid(rs.getInt("eid"));
                    lr.setTypeid(rs.getInt("typeid"));
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
        } finally {
            closeConnection();
        }

        return list;
    }

    @Override
    public ArrayList<BaseObject> list() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public BaseObject get(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void insert(BaseObject model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(BaseObject model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(BaseObject model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
