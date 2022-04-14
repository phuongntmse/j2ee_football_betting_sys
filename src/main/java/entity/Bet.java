package entity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Named
@RequestScoped
@Table(name = "BETS")
public class Bet implements Serializable {
    private String p_id;
    private String p_oddID;
    private String p_bettor;
    private int p_limcoins;
    private int p_status;
    private boolean fin;
    private String resultStr;
    private String betStr;
    private int benifit;
    public Bet() {
    }

    @Id
    @Column(name = "ID")
    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    @Column(name = "ODD_ID")
    public String getP_oddID() {
        return p_oddID;
    }

    public void setP_oddID(String p_oddID) {
        this.p_oddID = p_oddID;
    }

    @Column(name = "BETTOR")
    public String getP_bettor() {
        return p_bettor;
    }

    public void setP_bettor(String p_bettor) {
        this.p_bettor = p_bettor;
    }

    @Column(name = "LIMCOINS")
    public int getP_limcoins() {
        return p_limcoins;
    }

    public void setP_limcoins(int p_limcoins) {
        this.p_limcoins = p_limcoins;
    }

    @Column(name = "STATUS")
    public int getP_status() {
        return p_status;
    }

    public void setP_status(int p_status) {
        this.p_status = p_status;
    }
    @Transient
    public boolean isFin() {
        fin = getP_status() != -1;
        return fin;
    }
    public void setFin(boolean fin) {
        this.fin = fin;
    }
    @Transient
    public String getResultStr() {
        if (getP_status() == 1) {
            resultStr = "Win";
        } else if (getP_status() == 0) {
            resultStr = "Lose";
        } else resultStr = "Waiting for reuslt";
        return resultStr;
    }

    public void setResultStr(String resultStr) {
        this.resultStr = resultStr;
    }
    @Transient
    public String getBetStr() {
        return betStr;
    }

    public void setBetStr(String betStr) {
        this.betStr = betStr;
    }
    @Transient
    public int getBenifit() {
        return benifit;
    }

    public void setBenifit(int benifit) {
        this.benifit = benifit;
    }
}
