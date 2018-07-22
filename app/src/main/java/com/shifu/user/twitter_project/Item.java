package com.shifu.user.twitter_project;

public class Item {
    private int id;
    private String itemStr;

    public Item(int id, String itemStr) {
        this.id = id;
        this.itemStr = itemStr;
    }

    public String getItemStr() {
        return itemStr;
    }

    public int getId() {
        return id;
    }
}
