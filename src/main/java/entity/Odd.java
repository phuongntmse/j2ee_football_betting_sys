package entity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@RequestScoped
@Named
@Table(name = "ODDS")
public class Odd implements Serializable {
    private String o_id;
    private int o_type;
    private String o_matchID;
    private String o_owner;
    private float o_winodd;
    private String typeStr;

    public Odd() {
    }

    @Id
    @Column(name = "ID")
    public String getO_id() {
        return o_id;
    }

    public void setO_id(String o_id) {
        this.o_id = o_id;
    }

    @Column(name = "TYPE")
    public int getO_type() {
        return o_type;
    }

    public void setO_type(int o_type) {
        this.o_type = o_type;
    }

    @Column(name = "MATCH_ID")
    public String getO_matchID() {
        return o_matchID;
    }

    public void setO_matchID(String o_matchID) {
        this.o_matchID = o_matchID;
    }

    @Column(name = "USER_ID")
    public String getO_owner() {
        return o_owner;
    }

    public void setO_owner(String o_owner) {
        this.o_owner = o_owner;
    }

    @Column(name = "WIN_ODD")
    public float getO_winodd() {
        return o_winodd;
    }

    public void setO_winodd(float o_winodd) {
        this.o_winodd = o_winodd;
    }

    @Transient
    public String getTypeStr() {
        if (this.getO_type() == 0) {
            this.typeStr = "Team 1 Win";
        } else if (this.getO_type() == 1) {
            this.typeStr = "Team 2 Win";
        } else this.typeStr = "Tied";
        return this.typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }
}
