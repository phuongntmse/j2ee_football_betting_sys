package interfaces;

import entity.Match;
import entity.Odd;

import javax.ejb.Local;
import java.util.List;

@Local
public interface IBookmakerService {
    Match getSelectedMatch();

    String getMatchLabel();

    void createOdd(String userid, int type, float winodd) throws Exception;

    List<Odd> getOddsbyUserid(String userid);

    Match getMatchInformation(String mid);

    int getTotalBetsofOdd(String oddid);

    long getBenifitofOdd(String oddid, int matchrs);
}

