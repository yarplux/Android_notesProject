package com.shifu.user.twitter_project;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MessagesAuthor extends RealmObject {

    @PrimaryKey
    private String username;

    private String idToken;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
