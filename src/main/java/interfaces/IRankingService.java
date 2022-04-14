package interfaces;

import entity.User;

import javax.ejb.Local;
import java.util.List;

@Local
public interface IRankingService {
    List<User> getUserRank();
}
