package com.shifu.user.twitter_project;

import android.util.Log;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.annotations.PrimaryKey;
import io.realm.Realm;
import io.realm.RealmObject;

public class Messages  extends RealmObject {

    static final String FIELD_ID = "uuid_id";

    @PrimaryKey
    private String uuid_id;

    private String firebase_id;
    private String username;
    private String retwitted;
    private String text;
    private Long date;

    static Messages create(Realm realm) {
        MessagesList parent;

        if (realm.where(MessagesList.class).findFirst() == null) {
            parent = new MessagesList();
        } else {
            parent = realm.where(MessagesList.class).findFirst();
        }

        RealmList<Messages> items = parent.getItemList();
        Messages item;

        String id = UUID.randomUUID().toString();
        Log.d("Created UUID", id);
        item = realm.createObject(Messages.class, id);
        items.add(item);
        return item;
    }

    static void delete(Realm realm, String id) {
        Messages item = realm.where(Messages.class).equalTo(FIELD_ID, id).findFirst();
        if (item != null) {
            item.deleteFromRealm();
        }
    }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRetwitted() {
        return retwitted;
    }

    public void setRetwitted(String retwitted) {
        this.retwitted = retwitted;
    }
}
