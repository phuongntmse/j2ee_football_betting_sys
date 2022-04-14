package services;

import entity.User;
import interfaces.IRankingService;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.util.List;

@Local(IRankingService.class)
@Stateless
public class RankingService implements IRankingService {
    @EJB
    DBService dbService;

    @Override
    public List<User> getUserRank() {
        return dbService.getUsersRankingfromDB();
    }
}
