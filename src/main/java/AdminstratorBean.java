import entity.User;
import interfaces.IAdminstratorService;

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
public class AdminstratorBean implements Serializable {
    private DataModel<User> userDataModel;
    @EJB
    private IAdminstratorService adminstratorService;

    public DataModel<User> getUserDataModel() {
        setUserDataModel(new ListDataModel<>(adminstratorService.getListUser()));
        return userDataModel;
    }

    public void setUserDataModel(DataModel<User> userDataModel) {
        this.userDataModel = userDataModel;
    }

    public String updateUser() {
        User selectedUser = userDataModel.getRowData();
        int new_val = 1;
        if (selectedUser.getU_status() == 0) {
            new_val = 1;
        } else if (selectedUser.getU_status() == 1) {
            new_val = -1;
        }
        selectedUser.setU_status(new_val);
        try {
            adminstratorService.updateUser(selectedUser);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exception", e.getMessage()));
            return "failed";
        }
        return "success";
    }

    public String syncMatches() {
        try {
            adminstratorService.syncMatcheswithAPI();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exception", e.getMessage()));
            return "failed";
        }
        return "completed";
    }

    public String updateMatchesResult() {
        try {
            adminstratorService.syncMatchesRSwithAPI();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exception", e.getMessage()));
            return "failed";
        }
        return "completed";
    }
}
