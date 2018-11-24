package com.shifu.user.notes_project.realm;

import org.apache.commons.lang3.builder.ToStringBuilder;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MessagesUsers extends RealmObject {

    public static final String FIELD_ID = "uid";

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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("uid", uid)
                .append("username", username)
                .toString();
    }
}