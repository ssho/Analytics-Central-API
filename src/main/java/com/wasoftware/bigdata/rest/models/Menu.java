package com.wasoftware.bigdata.rest.models;

import javax.persistence.*;
import javax.ws.rs.DefaultValue;
import java.util.Date;

@Entity
@Table(name = "Menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String groupName;

    @Column(columnDefinition = "TEXT")
    private String menuObject;

    private Date createdAt;

    @DefaultValue("1")
    private int isActive;

    public Menu() {};

    public Menu(String groupName, String menuObject, int isActive) {
        this.groupName = groupName;
        this.menuObject = menuObject;
        this.isActive = isActive;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getGroupName() { return groupName; }

    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getMenuObject() { return menuObject; }

    public void setMenuObject(String menuObject) { this.menuObject = menuObject; }

    public Date getCreatedAt() { return createdAt; }

    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public int getIsActive() { return isActive; }

    public void setIsActive(int isActive) { this.isActive = isActive; }

    @Override
    public String toString() {
        return "id=" + getId()
                + " groupName=" + getGroupName()
                + " menuObject=" + getMenuObject()
                + " createdAt=" + getCreatedAt()
                + " isActive=" + getCreatedAt();
    }
}
