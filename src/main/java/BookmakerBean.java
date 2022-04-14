import entity.Match;
import entity.Odd;
import entity.User;
import interfaces.IBookmakerService;

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
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class BookmakerBean implements Serializable {
    private float win_odd1;
    private float win_odd2;
    private float win_odd3;
    private String matchLabel;
    private User user;
    private DataModel<OddObjectDisplay> myOddList;
    @EJB
    private IBookmakerService bookmakerService;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        user = (User) session.getAttribute("user_m");
        matchLabel = bookmakerService.getMatchLabel();
    }

    public String createOdd() {
        List<Float> win_odd = new ArrayList<>();
        win_odd.add(win_odd1);
        win_odd.add(win_odd2);
        win_odd.add(win_odd3);
        for (int i = 0; i < 3; i++) {
            try {
                bookmakerService.createOdd(user.getUsername(), i, win_odd.get(i));
            } catch (Exception e) {
                //clear cache
                win_odd1 = 0;
                win_odd2 = 0;
                win_odd3 = 0;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exception", e.getMessage()));
                return "failed";
            }
        }
        //clear cache
        win_odd1 = 0;
        win_odd2 = 0;
        win_odd3 = 0;
        return "success";
    }

    public DataModel<OddObjectDisplay> getMyOddList() {
        List<Odd> mylist = bookmakerService.getOddsbyUserid(user.getUsername());
        List<OddObjectDisplay> newlist = new ArrayList<OddObjectDisplay>();
        for (Odd myodd : mylist) {
            Match m = bookmakerService.getMatchInformation(myodd.getO_matchID());
            OddObjectDisplay oddobj = new OddObjectDisplay();
            oddobj.setMatchName(m.getM_team1() + " vs " + m.getM_team2());
            oddobj.setOddypt(myodd.getTypeStr());
            oddobj.setTotalBets(bookmakerService.getTotalBetsofOdd(myodd.getO_id()));
            oddobj.setBenefit(bookmakerService.getBenifitofOdd(myodd.getO_id(), m.getM_winner()));
            if (m.getM_status() == 0) oddobj.setStatus("Open");
            else oddobj.setStatus("Close");
            newlist.add(oddobj);
        }
        myOddList = new ListDataModel<OddObjectDisplay>(newlist);
        return myOddList;
    }

    public float getWin_odd1() {
        return win_odd1;
    }

    public void setWin_odd1(float win_odd1) {
        this.win_odd1 = win_odd1;
    }

    public float getWin_odd2() {
        return win_odd2;
    }

    public void setWin_odd2(float win_odd2) {
        this.win_odd2 = win_odd2;
    }

    public float getWin_odd3() {
        return win_odd3;
    }

    public void setWin_odd3(float win_odd3) {
        this.win_odd3 = win_odd3;
    }

    public String getMatchLabel() {
        matchLabel = bookmakerService.getMatchLabel();
        return matchLabel;
    }

    public void setMatchLabel(String matchLabel) {
        this.matchLabel = matchLabel;
    }

    public class OddObjectDisplay {
        private String matchName;
        private String oddypt;
        private int totalBets;
        private long benefit;
        private String status;

        public OddObjectDisplay() {
        }

        public String getMatchName() {
            return matchName;
        }

        public void setMatchName(String matchName) {
            this.matchName = matchName;
        }

        public String getOddypt() {
            return oddypt;
        }

        public void setOddypt(String oddypt) {
            this.oddypt = oddypt;
        }

        public int getTotalBets() {
            return totalBets;
        }

        public void setTotalBets(int totalBets) {
            this.totalBets = totalBets;
        }

        public long getBenefit() {
            return benefit;
        }

        public void setBenefit(long benefit) {
            this.benefit = benefit;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
