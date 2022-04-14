package services;

import entity.Match;
import entity.Odd;
import interfaces.IBookmakerService;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.util.List;

@Local(IBookmakerService.class)
@Stateless
public class BookmakerService implements IBookmakerService {
    @EJB
    private DBService dbService;

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
    public void createOdd(String userid, int type, float winodd) throws Exception {
        Match m = getSelectedMatch();
        String matchID = m.getM_id();
        Odd n_odd = new Odd();
        n_odd.setO_id(matchID + "-" + userid + "-" + type);
        n_odd.setO_matchID(matchID);
        n_odd.setO_type(type);
        n_odd.setO_winodd(winodd);
        n_odd.setO_owner(userid);
        if (dbService.checkOddAvailable(type, userid, matchID)) {
            try {
                dbService.addOdd(n_odd);
            } catch (Exception e) {
                throw e;
            }
        } else {
            throw new Exception("This Odd is exist " + matchID);
        }
    }

    @Override
    public List<Odd> getOddsbyUserid(String userid) {
        return dbService.getOddsOfBookmaker(userid);
    }

    @Override
    public Match getMatchInformation(String mid) {
        return dbService.getMatchInformation(mid);
    }

    @Override
    public int getTotalBetsofOdd(String oddid) {
        return dbService.getTotalBetsofOdd(oddid);
    }

    @Override
    public long getBenifitofOdd(String oddid, int matchrs) {
        return dbService.getBenifitofOdd(oddid, matchrs);
    }

}
