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
public class RoleDBContext extends DBContext<BaseObject> {

    public ArrayList<Role> getByUserId(int id) {

        ArrayList<Role> roles = new ArrayList<>();

        try {
            String sql = """
                                     SELECT r.rid,r.rolename,f.fid,f.url
                                     FROM [User] u INNER JOIN [UserRole] ur ON u.uid = ur.uid
                                     \t\t\t\t\t\tINNER JOIN [Role] r ON r.rid = ur.rid
                                     \t\t\t\t\t\tINNER JOIN [RoleFeature] rf ON rf.rid = r.rid
                                     \t\t\t\t\t\tINNER JOIN [Feature] f ON f.fid = rf.fid
                                     \t\t\t\t\t\tWHERE u.uid = ?""";

            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            Role current = new Role();
            current.setId(-1);
            while (rs.next()) {
                int rid = rs.getInt("rid");
                if (rid != current.getId()) {
                    current = new Role();
                    current.setId(id);
                    current.setRoleName(rs.getString("rname"));
                    roles.add(current);
                }
                Feature f = new Feature();
                f.setId(rs.getInt("fid"));
                f.setfUrl(rs.getString("url"));
                current.getFeatures().add(f);
            }

        } catch (SQLException ex) {
            Logger.getLogger(RoleDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close();
        }
        return roles;
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
