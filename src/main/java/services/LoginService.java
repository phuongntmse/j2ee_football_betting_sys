package services;

import entity.User;
import interfaces.ILoginService;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@Local(ILoginService.class)
@Stateless
public class LoginService implements ILoginService {
    @EJB
    private DBService dbService;

    @Override
    public void login(String uid, String pwd) throws Exception {
        if (dbService.checkAuth(uid, pwd)) {
            User user = dbService.getUserInformation(uid);
            if (user.getU_status() == -1) {
                throw new Exception("You have been banned from our system");
            }
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
            session.setAttribute("user_m", user);
        } else throw new Exception("Your input is incorrect!");
    }

    @Override
    public void logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        session.removeAttribute("user_m");
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }

    @Override
    public void register(String uid, String pwd, int role) throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        if (dbService.getUserInformation(uid) != null) {
            throw new Exception("Username already exist!");
        } else {
            User n_u = new User();
            n_u.setUsername(uid);
            n_u.setPassword(pwd);
            n_u.setU_role(role);
            n_u.setU_status(0);
            n_u.setU_limcoins(1000);
            try {
                dbService.register(n_u);
                HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
                session.setAttribute("user_m", n_u);
            } catch (Exception e) {
                throw e;
            }
        }
    }

    @Override
    public boolean havePermission(int role) {
        if (isLogined()) {
            return (getUser().getU_role() == role);
        }
        return false;
    }

    @Override
    public boolean isLogined() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        return (session.getAttribute("user_m") != null);
    }

    @Override
    public boolean isActive() {
        if (isLogined()) {
            return getUser().getU_status() == 1;
        }
        return false;
    }

    @Override
    public int getMaxLimCoins() {
        if (isLogined()) {
            return getUser().getU_limcoins();
        }
        return -1;
    }

    @Override
    public User getUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        User user = (User) session.getAttribute("user_m");
        user = dbService.getUserInformation(user.getUsername());
        session.setAttribute("user_m", user);
        return user;
    }
}
