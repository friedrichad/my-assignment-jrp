/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.BaseObject;
import model.self.Feature;
import model.self.Role;

/**
 *
 * @author Hiro
 */
public class RoleDBContext extends DBContext<Role> {

    public ArrayList<Role> getByUserId(int userId) {
    ArrayList<Role> roles = new ArrayList<>();
    String sql = """
        SELECT r.rid AS rid, r.roleName, f.fid AS fid, f.url AS fUrl
                FROM [Role] r
                INNER JOIN [UserRole] ur ON ur.rid = r.rid
                LEFT JOIN [RolePermission] rf ON rf.rid = r.rid
                LEFT JOIN [Feature] f ON f.fid = rf.fid
                WHERE ur.uid = ?
    """;

    try (PreparedStatement stm = connection.prepareStatement(sql)) {
        stm.setInt(1, userId);
        ResultSet rs = stm.executeQuery();

        int currentRoleId = -1;
        Role currentRole = null;

        while (rs.next()) {
            int rid = rs.getInt("rid");

            // Nếu role mới → tạo mới và thêm vào danh sách
            if (currentRole == null || rid != currentRoleId) {
                currentRole = new Role();
                currentRole.setId(rid);
                currentRole.setRoleName(rs.getString("roleName"));
                currentRole.setFeatures(new ArrayList<>());
                roles.add(currentRole);
                currentRoleId = rid;
            }

            // Thêm feature vào role hiện tại nếu có
            int fid = rs.getInt("fid");
            String fUrl = rs.getString("fUrl");
            if (fid > 0 && fUrl != null) {
                Feature f = new Feature();
                f.setId(fid);
                f.setfUrl(fUrl);
                currentRole.getFeatures().add(f);
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return roles;
}

    @Override
    public void insert(Role model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(Role model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(Role model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<Role> list() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Role get(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
