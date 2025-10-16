/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object;
import java.sql.Date;
/**
 *
 * @author Hiro
 */
public class Employee extends BaseObject {
    private String userRealName;
    private boolean gender;
    private java.sql.Date dob;
    private String email;
    private String role;

    public Employee(String userRealName, boolean gender, Date dob, String email, String role) {
        this.userRealName = userRealName;
        this.gender = gender;
        this.dob = dob;
        this.email = email;
        this.role = role;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    
}
