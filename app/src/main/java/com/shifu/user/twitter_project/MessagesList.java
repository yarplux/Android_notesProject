package com.shifu.user.twitter_project;

import io.realm.RealmList;
import io.realm.RealmObject;

public class MessagesList extends RealmObject {
    @SuppressWarnings("unused")
    private RealmList<Messages> itemList = new RealmList<>();

    public RealmList<Messages> getItemList() {
        return itemList;
    }
}