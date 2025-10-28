/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import model.LeaveRequest;
import java.sql.*;
import java.util.ArrayList;
import model.BaseObject;

public class LeaveRequestDBContext extends DBContext<BaseObject> {

    public void insert(LeaveRequest lr) {
        String sql = "INSERT INTO LeaveRequest(eid, typeid, start_date, end_date, num_days, status, approverid, requested_at) "
                   + "VALUES (?, ?, ?, ?, ?, DEFAULT, NULL, DEFAULT)";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, lr.getEid());
            stm.setInt(2, lr.getTypeid());
            stm.setDate(3, new java.sql.Date(lr.getStartDate().getTime()));
            stm.setDate(4, new java.sql.Date(lr.getEndDate().getTime()));
            stm.setDouble(5, lr.getNumDays());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
