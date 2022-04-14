import interfaces.ILoginService;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginBean implements Serializable {
    private String username;
    private String password;
    private int role;
    @EJB
    private ILoginService loginService;

    public LoginBean() {
    }

    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            loginService.login(this.username, this.password);
            return "success";
        } catch (Exception e) {
            context.addMessage("loginForm:pwdInput", new FacesMessage(e.getMessage()));
            return "failed";
        }
    }

    public String logout() {
        loginService.logout();
        return "success";
    }

    public String register() {
        try {
            loginService.register(this.username, this.password, this.role);
            return "success";
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("registerForm:name", new FacesMessage(e.getMessage()));
            return "failed";
        }
    }

    private boolean havePermission(int role) {
        return loginService.havePermission(role);
    }

    public boolean isAdmin() {
        return havePermission(0);
    }

    public boolean isBookmaker() {
        return havePermission(2);
    }

    public boolean isBettor() {
        return havePermission(1);
    }

    public int getMaxLimcoins() {
        return loginService.getMaxLimCoins();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
