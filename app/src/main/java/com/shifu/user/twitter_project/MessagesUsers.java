package com.shifu.user.twitter_project;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MessagesUsers extends RealmObject {

    static final String FIELD_ID = "uid";

    @PrimaryKey
    private String uid;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }
}