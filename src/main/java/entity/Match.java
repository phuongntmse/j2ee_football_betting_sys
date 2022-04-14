package entity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@RequestScoped
@Named
@Table(name = "MATCHES")
public class Match implements Serializable {
    private String m_id;
    private String m_competition;
    private String m_team1;
    private String m_team2;
    private int m_status;
    private String m_sdate;
    private int m_winner;
    private String m_time;

    public Match() {
    }

    @Id
    @Column(name = "ID")
    public String getM_id() {
        return m_id;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }

    @Column(name = "TEAM1")
    public String getM_team1() {
        return m_team1;
    }

    public void setM_team1(String m_team1) {
        this.m_team1 = m_team1;
    }

    @Column(name = "TEAM2")
    public String getM_team2() {
        return m_team2;
    }

    public void setM_team2(String m_team2) {
        this.m_team2 = m_team2;
    }


    @Column(name = "STARTDATE")

    public String getM_sdate() {
        return m_sdate;
    }

    public void setM_sdate(String m_sdate) {
        this.m_sdate = m_sdate;
    }

    @Column(name = "COMPETITION")
    public String getM_competition() {
        return m_competition;
    }

    public void setM_competition(String m_competition) {
        this.m_competition = m_competition;
    }

    @Column(name = "STATUS")
    public int getM_status() {
        return m_status;
    }

    public void setM_status(int m_status) {
        this.m_status = m_status;
    }

    @Column(name = "WINNER")
    public int getM_winner() {
        return m_winner;
    }

    public void setM_winner(int m_winner) {
        this.m_winner = m_winner;
    }

    @Transient
    public String getM_time() {
        ZonedDateTime m_startTime = ZonedDateTime.parse(this.getM_sdate());
        m_time = m_startTime.getDayOfMonth() + "-" + m_startTime.getMonthValue() + "-" + m_startTime.getYear() + " "
                + m_startTime.getHour() + ":" + m_startTime.getMinute() + ":00";
        return m_time;
    }

    public void setM_time(String m_time) {
        this.m_time = m_time;
    }
}
