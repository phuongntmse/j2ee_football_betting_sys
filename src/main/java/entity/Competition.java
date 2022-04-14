package entity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@RequestScoped
@Named
@Table(name = "COMPETITIONS")
public class Competition implements Serializable {
    private String c_id;
    private String c_name;

    @Id
    @Column(name = "ID")
    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    @Column(name = "NAME")
    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }
}
