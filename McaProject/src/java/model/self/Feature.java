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
public class Feature extends BaseObject{
    private String fUrl;
    private ArrayList<Role> roles = new ArrayList<>();

    public String getfUrl() {
        return fUrl;
    }

    public void setfUrl(String fUrl) {
        this.fUrl = fUrl;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Role> roles) {
        this.roles = roles;
    }
    
}
