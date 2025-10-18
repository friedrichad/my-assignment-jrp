/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.self;

import model.BaseObject;
import model.Employee;
import java.util.ArrayList;
/**
 *
 * @author Hiro
 */
public class UserAccount extends BaseObject{
    private String displayName;
    private String userName;
    private String password;
    private Employee employee;
    private ArrayList<Role> roles = new ArrayList<>();

    public UserAccount(int id,String displayName, String userName, String password) {
        this.setId(id);
        this.displayName = displayName;
        this.userName = userName;
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Role> roles) {
        this.roles = roles;
    }
}
