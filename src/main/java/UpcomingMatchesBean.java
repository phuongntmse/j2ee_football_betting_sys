import entity.Match;
import interfaces.IUpcomingMatchesService;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class UpcomingMatchesBean implements Serializable {
    private DataModel<Match> matchDataModel;
    @EJB
    private IUpcomingMatchesService upcomingMatchesService;

    public String betThisMatch() {
        Match selectedMatch = matchDataModel.getRowData();
        try {
            upcomingMatchesService.betThisMatch(selectedMatch);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", e.getMessage()));
            return "notfound";
        }
        return "betting";
    }

    public String createOdd() {
        Match selectedMatch = matchDataModel.getRowData();
        try {
            upcomingMatchesService.createOdd(selectedMatch);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", e.getMessage()));
            return "alreadyexist";
        }
        return "newodd";
    }

    public DataModel<Match> getMatchDataModel() {
        setMatchDataModel(new ListDataModel<>(upcomingMatchesService.getNearMatches()));
        return matchDataModel;
    }

    public void setMatchDataModel(DataModel<Match> matchDataModel) {
        this.matchDataModel = matchDataModel;
    }
}
