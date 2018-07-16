package com.shifu.user.twitter_project;

import io.realm.annotations.PrimaryKey;
import io.realm.RealmObject;

public class Messages  extends RealmObject {

    static final String FIELD_ID = "uuid_id";

    @PrimaryKey
    private String uuid_id;

    private String firebase_id;
    private String uid;
    private String retwitted;
    private String text;
    private Long date;

    public String getID() {
        return uuid_id;
    }

    public String getText() {
        return text;
    }
    public void setText(String data) {
        this.text = data;
    }

    public Long getDate() {
        return date;
    }
    public void setDate(Long data) {
        this.date = data;
    }

    public String getFirebase_id() {
        return firebase_id;
    }
    public void setFirebase_id(String firebase_id) {
        this.firebase_id = firebase_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
