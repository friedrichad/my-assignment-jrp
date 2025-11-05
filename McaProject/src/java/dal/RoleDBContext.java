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

    public ArrayList<Role> getByUserId(int id) {
        ArrayList<Role> roles = new ArrayList<>();
        System.out.println(">>> [DEBUG] Fetching roles for userId = " + id);

        try {
            String sql = """
            SELECT r.rid, r.rolename, f.fid, f.url
            FROM [User] u 
            INNER JOIN [UserRole] ur ON u.uid = ur.uid
            INNER JOIN [Role] r ON r.rid = ur.rid
            INNER JOIN [RolePermission] rf ON rf.rid = r.rid
            INNER JOIN [Feature] f ON f.fid = rf.fid
            WHERE u.uid = ?
        """;

            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                int rid = rs.getInt("rid");
                String rolename = rs.getString("rolename");
                int fid = rs.getInt("fid");
                String furl = rs.getString("url"); // kiểm tra tên cột trong DB nhé

                Role current = null;
                for (Role r : roles) {
                    if (r.getId() == rid) {
                        current = r;
                        break;
                    }
                }

                if (current == null) {
                    current = new Role();
                    current.setId(rid);
                    current.setRoleName(rolename);
                    current.setFeatures(new ArrayList<>());
                    roles.add(current);
                    System.out.println(">>> [DEBUG] Found new role: " + rolename);
                }

                Feature f = new Feature();
                f.setId(fid);
                f.setfUrl(furl);
                current.getFeatures().add(f);
                System.out.println("    -> [DEBUG] Added feature: " + furl);
            }

            System.out.println(">>> [DEBUG] Total roles loaded: " + roles.size());

        } catch (SQLException ex) {
            Logger.getLogger(RoleDBContext.class.getName()).log(Level.SEVERE, null, ex);
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
