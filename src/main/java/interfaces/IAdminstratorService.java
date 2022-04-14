package interfaces;

import entity.User;

import javax.ejb.Local;
import java.util.List;

@Local
public interface IAdminstratorService {
    void updateUser(User n_user) throws Exception;

    void syncMatcheswithAPI() throws Exception;

    void syncMatchesRSwithAPI() throws Exception;

    List<User> getListUser();
}
