package com.shifu.user.twitter_project;

import android.util.Log;

import java.util.concurrent.atomic.AtomicLong;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MessagesUsers extends RealmObject {

    public static final String FIELD_ID = "id";
    private static AtomicLong INTEGER_COUNTER = new AtomicLong(0L);

    @PrimaryKey
    private long id;

    private String username;

    private static long increment() {
        return INTEGER_COUNTER.getAndIncrement();
    }

    static MessagesUsers create(Realm realm) {
        return realm.createObject(MessagesUsers.class, increment());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getId() {
        return id;
    }
}