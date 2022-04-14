package services;

import entity.Match;
import entity.Odd;
import interfaces.ILoginService;
import interfaces.IUpcomingMatchesService;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.util.List;

@Local(IUpcomingMatchesService.class)
@Stateless
public class UpcomingMatchesService implements IUpcomingMatchesService {
    @EJB
    DBService dbService;
    @EJB
    ILoginService loginService;

    @Override
    public List<Match> getNearMatches() {
        return dbService.getUpcomingMatches();
    }

    @Override
    public void betThisMatch(Match match) throws Exception {
        if (!loginService.isActive()) throw new Exception("You must waiting for Admin active your account");
        List<Odd> od = dbService.getOddsOfMatch(match.getM_id());
        if (od.size() == 0) {
            throw new Exception("Please wait for bookmaker create odd for this match");
        }
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session.setAttribute("betmatch", match);
    }

    @Override
    public void createOdd(Match match) throws Exception {
        if (!loginService.isActive()) throw new Exception("You must waiting for Admin active your account");
        String uid = loginService.getUser().getUsername();
        String mid = match.getM_id();
        if (dbService.checkOddMatchAvailable(uid, mid)) {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            session.setAttribute("betmatch", match);
        } else throw new Exception("You already create odds for this match!");
    }
}
