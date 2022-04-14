package entity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@RequestScoped
@Named
@Table(name = "USERS")
public class User implements Serializable {
    private String username;
    private String password;
    private int u_role;
    private int u_limcoins;
    private int u_status;
    private String roleStr;
    private String statusStr;
    private String modifyStr;

    public User() {
    }

    @Id
    @Column(name = "USERNAME")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "ROLE")
    public int getU_role() {
        return u_role;
    }

    public void setU_role(int u_role) {
        this.u_role = u_role;
    }

    @Column(name = "LIMCOINS")
    public int getU_limcoins() {
        return u_limcoins;
    }

    public void setU_limcoins(int limcoins) {
        this.u_limcoins = limcoins;
    }

    @Column(name = "PASSWORD")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "STATUS")
    public int getU_status() {
        return u_status;
    }

    public void setU_status(int status) {
        this.u_status = status;
    }

    @Transient
    public String getRoleStr() {
        if (this.u_role == 1) return "Bettor";
        if (this.u_role == 2) return "Bookmaker";
        return "Admin";
    }

    public void setRoleStr(String roleStr) {
        this.roleStr = roleStr;
    }

    @Transient
    public String getStatusStr() {
        if (this.u_status == 0) return "New";
        if (this.u_status == 1) return "Active";
        return "Banned";
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    @Transient
    public String getModifyStr() {
        if (this.u_status == 0) return "Active";
        if (this.u_status == 1) return "Ban";
        return "Re-active";
    }

    public void setModifyStr(String modifyStr) {
        this.modifyStr = modifyStr;
    }
}
