package interfaces;

import entity.User;

import javax.ejb.Local;

@Local
public interface ILoginService {
    void login(String uid, String pwd) throws Exception;

    void logout();

    void register(String uid, String pwd, int role) throws Exception;

    boolean havePermission(int role);

    boolean isLogined();

    boolean isActive();

    int getMaxLimCoins();

    User getUser();
}
