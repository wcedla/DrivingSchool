package com.wcedla.driving_school.bean;

public class AdminItemAdapterBean {

    private int itemIcon;

    private String itemName;

    public AdminItemAdapterBean(int itemIcon, String itemName) {
        this.itemIcon = itemIcon;
        this.itemName = itemName;
    }

    public int getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(int itemIcon) {
        this.itemIcon = itemIcon;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
