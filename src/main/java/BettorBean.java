import entity.Bet;
import entity.Odd;
import entity.User;
import interfaces.IBettorService;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class BettorBean implements Serializable {
    private String matchLabel;
    private User user;
    private int limcoins;
    private DataModel<Bet> myBetList;
    private List<String> listBookmaker;
    private List<Odd> oddList;
    private float profit;
    private String bookmakerid;
    private String oddid;
    @EJB
    private IBettorService bettorService;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        user = (User) session.getAttribute("user_m");
        matchLabel = bettorService.getMatchLabel();
    }

    public String betting() {
        try {
            bettorService.Betting(user.getUsername(), oddid, limcoins);
        } catch (Exception e) {
            //clear cache
            limcoins = 0;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exception", e.getMessage()));
            return "failed";
        }
        //clear cache
        limcoins = 0;
        profit = (float) 0.0;
        return "success";
    }

    public DataModel<Bet> getMyBetList() {
        myBetList = new ListDataModel<Bet>(bettorService.getMyBetList(user.getUsername()));
        return myBetList;
    }

    public String getMatchLabel() {
        matchLabel = bettorService.getMatchLabel();
        return matchLabel;
    }

    public void setMatchLabel(String matchLabel) {
        this.matchLabel = matchLabel;
    }

    public int getLimcoins() {
        return limcoins;
    }

    public void setLimcoins(int limcoins) {
        this.limcoins = limcoins;
    }

    public float getProfit() {
        return bettorService.getRelatedProfit(oddid);
    }

    public void setProfit(float profit) {
        this.profit = profit;
    }

    public List<String> getListBookmaker() {
        return bettorService.getBookmakerAvailable();
    }

    public String getBookmakerid() {
        return bookmakerid;
    }

    public void setBookmakerid(String bookmakerid) {
        this.bookmakerid = bookmakerid;
    }

    public List<Odd> getOddList() {
        return bettorService.getRelatedOdd(this.bookmakerid);
    }

    public void setOddList(List<Odd> oddList) {
        this.oddList = oddList;
    }

    public String getOddid() {
        return oddid;
    }

    public void setOddid(String oddid) {
        this.oddid = oddid;

    }
}
