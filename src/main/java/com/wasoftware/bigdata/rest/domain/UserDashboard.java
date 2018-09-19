package com.wasoftware.bigdata.rest.domain;

import java.util.Date;

public class UserDashboard {

    public long dashboardID;
    public String dashboardName = "";
    public String dashboardObject = "";
    public Date createdat;

    public UserDashboard(long dashboardID, String dashboardName, String dashboardObject , Date createat) {
        this.dashboardID = dashboardID;
        this.dashboardName = dashboardName;
        this.dashboardObject = dashboardObject;
        this.createdat = createat;
    }

    public long getDashboardID() {
        return dashboardID;
    }

    public void setDashboardID(long dashboardID) {
        this.dashboardID = dashboardID;
    }

    public String getDashboardName() {
        return dashboardName;
    }

    public void setDashboardName(String dashboardName) {
        this.dashboardName = dashboardName;
    }

    public String getDashboardObject() {
        return dashboardObject;
    }

    public void setDashboardObject(String dashboardObject) {
        this.dashboardObject = dashboardObject;
    }

    public Date getCreatedat() {  return createdat; }

    public void setCreatedat(Date createdat) {  this.createdat = createdat;  }
}
