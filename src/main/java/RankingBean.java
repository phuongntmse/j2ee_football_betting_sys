import entity.User;
import interfaces.IRankingService;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class RankingBean implements Serializable {
    private DataModel<User> userDataModel;
    @EJB
    private IRankingService rankingService;
    public DataModel<User> getUserDataModel() {
        setUserDataModel(new ListDataModel<User>(rankingService.getUserRank()));
        return userDataModel;
    }

    public void setUserDataModel(DataModel<User> userDataModel) {
        this.userDataModel = userDataModel;
    }
}
