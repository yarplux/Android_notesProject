package com.shifu.user.twitter_project.realm;

import org.apache.commons.lang3.builder.ToStringBuilder;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MessagesAuthor extends RealmObject {

    public static final String FIELD_ID = "uid";

    @PrimaryKey
    private String uid;

    private String username;
    private String idToken;
    private String refreshToken;

    static void delete(Realm realm, String id) {
        MessagesAuthor item = realm.where(MessagesAuthor.class).equalTo(FIELD_ID, id).findFirst();
        if (item != null) {
            item.deleteFromRealm();
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("uid", uid)
                .append("username", username)
                .append("idToken", idToken)
                .append("refreshToken", refreshToken)
                .toString();
    }}
