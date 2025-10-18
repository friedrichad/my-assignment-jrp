/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.self;
import java.util.ArrayList;
import model.BaseObject;
/**
 *
 * @author Hiro
 */
public class Role extends BaseObject{
    private String roleName;
    private ArrayList<UserAccount> users = new ArrayList<>();
    private ArrayList<Feature> features = new ArrayList<>();

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public ArrayList<UserAccount> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UserAccount> users) {
        this.users = users;
    }

    public ArrayList<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(ArrayList<Feature> features) {
        this.features = features;
    }
    
}
