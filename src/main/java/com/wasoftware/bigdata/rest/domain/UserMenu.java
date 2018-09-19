package com.wasoftware.bigdata.rest.domain;

import java.util.Date;

public class UserMenu {

    public long menuId;
    public String menuObject = "";
    public Date createdAt;

    public UserMenu(long menuId, String menuObject, Date createdAt) {
        this.menuId = menuId;
        this.menuObject = menuObject;
        this.createdAt = createdAt;
    }

    public long getMenuId() { return menuId; }

    public void setMenuId(long menuId) { this.menuId = menuId; }

    public String getMenuObject() { return menuObject; }

    public void setMenuObject(String menuObject) { this.menuObject = menuObject; }

    public Date getCreatedAt() { return createdAt; }

    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
