package com.wasoftware.bigdata.rest.models;


import javax.persistence.*;
import javax.ws.rs.DefaultValue;
import java.util.Date;

@Entity
@Table(name = "dashboard")
public class Dashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @DefaultValue("0")
    private long userid;

    private String dashboardname;

    @DefaultValue("1")
    private int active;

    @DefaultValue("")

    @Column(columnDefinition = "TEXT")
    private String dashboardobject;

    private Date createdat;

    private Boolean isdefault;

    private Boolean issystemdefault;

    public Dashboard() {}

    public Dashboard(long userid, String dashboardName, int active, String dashboardObject, Boolean isdefault, Boolean issystemdefault) {
        this.userid = userid;
        this.dashboardname = dashboardName;
        this.active = active;
        this.dashboardobject = dashboardObject;
        this.isdefault = isdefault;
        this.issystemdefault = issystemdefault;
    }

    @Override
    public String toString() {
        return "Dashboard{" +
                "id=" + id +
                ", userid=" + userid +
                ", dashboardName='" + dashboardname + '\'' +
                ", active=" + active +
                ", dashboardObject='" + dashboardobject + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getDashboardname() {
        return dashboardname;
    }

    public void setDashboardname(String dashboardname) {
        this.dashboardname = dashboardname;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getDashboardobject() {
        return dashboardobject;
    }

    public void setDashboardobject(String dashboardobject) {
        this.dashboardobject = dashboardobject;
    }

    public Date getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Date createdat) {
        this.createdat = createdat;
    }

    public Boolean getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(Boolean isdefault) {
        this.isdefault = isdefault;
    }

    public Boolean getIssystemdefault() {
        return issystemdefault;
    }

    public void setIssystemdefault(Boolean issystemdefault) {
        this.issystemdefault = issystemdefault;
    }
}
