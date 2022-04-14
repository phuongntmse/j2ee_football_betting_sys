package interfaces;

import entity.Bet;
import entity.Match;
import entity.Odd;

import javax.ejb.Local;
import java.util.List;

@Local
public interface IBettorService {
    void Betting(String userid, String oddid, int limcoins) throws Exception;

    List<Bet> getMyBetList(String userid);

    Match getSelectedMatch();

    String getMatchLabel();

    List<String> getBookmakerAvailable();

    List<Odd> getRelatedOdd(String uid);

    Float getRelatedProfit(String odd);
}
