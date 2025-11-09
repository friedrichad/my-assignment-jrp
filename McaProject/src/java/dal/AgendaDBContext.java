package dal;

import model.LeaveRequest;
import model.Employee;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import model.LeaveType;

/**
 * AgendaDBContext - Chuyên xử lý hiển thị lịch nghỉ phép
 */
public class AgendaDBContext extends DBContext<LeaveRequest> {

    private final UserDBContext userDB = new UserDBContext();

    // ===================================================================
    // 1. LẤY NGHỈ PHÉP CỦA MỘT NHÂN VIÊN TRONG KHOẢNG THỜI GIAN
    // ===================================================================
    public ArrayList<LeaveRequest> getRequestsByEmployeeInRange(int eid, Date from, Date to) {
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
              AND lr.start_date <= ? AND lr.end_date >= ?
            ORDER BY lr.requested_at DESC
        """;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, eid);
            stm.setDate(2, to);
            stm.setDate(3, from);
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

    // ===================================================================
    // 2. LẤY LỊCH CÁ NHÂN (THEO THÁNG/NĂM)
    // ===================================================================
    public ArrayList<LeaveRequest> getPersonalAgenda(int eid, int month, int year) {
        Date from = getFirstDayOfMonth(year, month);
        Date to = getLastDayOfMonth(year, month);
        return getRequestsByEmployeeInRange(eid, from, to);
    }

    // ===================================================================
    // 3. LẤY LỊCH QUẢN LÝ (BẢN THÂN + CẤP DƯỚI)
    // ===================================================================
    public ArrayList<LeaveRequest> getManagerAgenda(int managerEid, int month, int year) {
        Date from = getFirstDayOfMonth(year, month);
        Date to = getLastDayOfMonth(year, month);

        ArrayList<LeaveRequest> allLeaves = new ArrayList<>();

        // Lấy danh sách cấp dưới + bản thân
        ArrayList<Employee> subordinates = userDB.getAllSubordinates(managerEid);
        Employee self = new Employee();
        self.setId(managerEid);
        subordinates.add(0, self); // thêm bản thân vào đầu

        for (Employee emp : subordinates) {
            ArrayList<LeaveRequest> empLeaves = getRequestsByEmployeeInRange(emp.getId(), from, to);
            allLeaves.addAll(empLeaves);
        }

        return allLeaves;
    }

    // ===================================================================
    // HELPER: TÍNH NGÀY ĐẦU / CUỐI THÁNG
    // ===================================================================
    private Date getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Date(cal.getTimeInMillis());
    }

    private Date getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return new Date(cal.getTimeInMillis());
    }
    // 1. Đếm tổng số đơn
public int countLeavesByEmployeesAndDateRange(List<Integer> eids, LocalDate from, LocalDate to) {
    if (eids.isEmpty()) return 0;
    String inClause = eids.stream().map(String::valueOf).collect(Collectors.joining(","));
    String sql = """
        SELECT COUNT(*) 
        FROM LeaveRequest 
        WHERE eid IN (%s) 
          AND start_date <= ? 
          AND end_date >= ?
        """.formatted(inClause);

    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setDate(1, java.sql.Date.valueOf(to));
        ps.setDate(2, java.sql.Date.valueOf(from));
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt(1) : 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return 0;
    }
}

// 2. Lấy đơn có phân trang
public List<LeaveRequest> getLeavesByEmployeesAndDateRangePaged(
        List<Integer> eids, LocalDate from, LocalDate to, int offset, int limit) {
    
    List<LeaveRequest> leaves = new ArrayList<>();
    if (eids.isEmpty()) return leaves;

    String inClause = eids.stream().map(String::valueOf).collect(Collectors.joining(","));
    String sql = """
        SELECT lr.*, lt.typename, e.ename AS employee_name
        FROM LeaveRequest lr
        JOIN LeaveType lt ON lr.typeid = lt.typeid
        JOIN Employee e ON lr.eid = e.eid
        WHERE lr.eid IN (%s)
          AND lr.start_date <= ?
          AND lr.end_date >= ?
        ORDER BY lr.start_date DESC
        OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
        """.formatted(inClause);

    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        // Chuyển LocalDate → java.sql.Date
        ps.setDate(1, java.sql.Date.valueOf(to));
        ps.setDate(2, java.sql.Date.valueOf(from));
        ps.setInt(3, offset);
        ps.setInt(4, limit);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            LeaveRequest lr = new LeaveRequest();

            lr.setId(rs.getInt("reqid"));
            lr.setEid(rs.getInt("eid"));
            lr.setTypeid(rs.getInt("typeid"));
            lr.setLeaveTypeName(rs.getString("typename"));  // đúng

            // === Employee Object ===
            Employee emp = new Employee();
            emp.setId(rs.getInt("eid"));
            emp.setEmployeeName(rs.getString("employee_name"));
            lr.setEmployee(emp);  // đúng

            // === Date (java.util.Date) ===
            lr.setStartDate(rs.getDate("start_date"));   // rs.getDate() → java.util.Date
            lr.setEndDate(rs.getDate("end_date"));       // tự động đúng kiểu

            lr.setNumDays(rs.getBigDecimal("num_days").doubleValue());
            lr.setStatus(rs.getString("status"));
            lr.setReason(rs.getString("reason"));
            lr.setApproverid(rs.getObject("approverid") != null 
                ? rs.getInt("approverid") : null);
            lr.setRequestedAt(rs.getTimestamp("requested_at"));

            leaves.add(lr);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return leaves;
}
    // ===================================================================
    // OVERRIDE ABSTRACT METHODS (KHÔNG DÙNG)
    // ===================================================================
    @Override
    public ArrayList<LeaveRequest> list() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public LeaveRequest get(int id) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void insert(LeaveRequest model) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void update(LeaveRequest model) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void delete(LeaveRequest model) {
        throw new UnsupportedOperationException("Not supported.");
    }
}