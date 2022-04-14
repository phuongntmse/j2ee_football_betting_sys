package services;

import entity.Bet;
import entity.Match;
import entity.Odd;
import entity.User;
import interfaces.IBettorService;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Local(IBettorService.class)
@Stateless
public class BettorService implements IBettorService {
    @EJB
    private DBService dbService;

    @Override
    public void Betting(String userid, String oddid, int limcoins) throws Exception {
        try {
            Match m = getSelectedMatch();
            String matchID = m.getM_id();
            Odd selectedOdd = dbService.getOddInformation(oddid);
            if (selectedOdd != null) {
                if (dbService.checkBetAvailabel(userid, selectedOdd.getO_id())) {
                    Bet n_bet = new Bet();
                    n_bet.setP_id(userid + "" + selectedOdd.getO_id());
                    n_bet.setP_oddID(selectedOdd.getO_id());
                    n_bet.setP_bettor(userid);
                    n_bet.setP_limcoins(limcoins);
                    n_bet.setP_status(-1);
                    dbService.addBet(n_bet);
                    //sent bet limcoins to bookmaker
                    String bookmakerid = selectedOdd.getO_owner();
                    User bookmaker = dbService.getUserInformation(bookmakerid);
                    bookmaker.setU_limcoins(bookmaker.getU_limcoins() + limcoins);
                    dbService.updateUser(bookmaker);
                } else {
                    throw new Exception("You already bet this game at this odd!");
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<Bet> getMyBetList(String userid) {
        List<Bet> mb = dbService.getBetsofUser(userid);
        List<Bet> newList = new ArrayList<Bet>();
        for (Bet b : mb) {
            Odd o = dbService.getOddInformation(b.getP_oddID());
            Match m = dbService.getMatchInformation(o.getO_matchID());
            String betString = m.getM_team1() + " vs " + m.getM_team2() + "(" + o.getTypeStr() + ")";
            b.setBetStr(betString);
            float benifit = b.getP_limcoins() * o.getO_winodd();
            if (b.getP_status() == 0) benifit = -1 * b.getP_limcoins();
            b.setBenifit((int) benifit);
            newList.add(b);
        }
        return newList;
    }

    @Override
    public Match getSelectedMatch() {
        Match rs = new Match();
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        if (session.getAttribute("betmatch") != null) {
            rs = (Match) session.getAttribute("betmatch");
        }
        return rs;
    }

    @Override
    public String getMatchLabel() {
        Match selectedMatch = getSelectedMatch();
        if (selectedMatch != null) {
            return selectedMatch.getM_team1() + " vs " + selectedMatch.getM_team2();
        } else return "";
    }

    @Override
    public List<String> getBookmakerAvailable() {
        return dbService.getBookmakerOfMatch(getSelectedMatch().getM_id());
    }

    @Override
    public List<Odd> getRelatedOdd(String uid) {
        if (uid == null) {
            String dummyuid = getBookmakerAvailable().get(0);
            return dbService.getRelatedOdd(getSelectedMatch().getM_id(), dummyuid);
        }
        return dbService.getRelatedOdd(getSelectedMatch().getM_id(), uid);
    }

    @Override
    public Float getRelatedProfit(String odd) {
        if (odd == null) {
            Odd newodd = getRelatedOdd(null).get(0);
            return newodd.getO_winodd();
        }
        return dbService.getOddInformation(odd).getO_winodd();
    }
}
