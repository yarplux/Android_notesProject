package com.shifu.user.twitter_project;

import java.util.Date;

import io.realm.RealmList;
import io.realm.annotations.PrimaryKey;

import io.realm.Realm;
import io.realm.RealmObject;

public class Messages  extends RealmObject {

    public static final String FIELD_ID = "date";

    @PrimaryKey
    private Long date;
    private String text = "";

    public String getText() { return text; }
    public void setText(String data) { this.text = data; }

    public Long getDate() { return date; }
    public void setDate(Long data) { this.date = data; }

    //  create() & delete() needs to be called inside a transaction.

    static Messages create(Realm realm) {
        MessagesList parent;

        if (realm.where(MessagesList.class).findFirst() == null) {
            parent = new MessagesList();
        } else {
            parent = realm.where(MessagesList.class).findFirst();
        }

        RealmList<Messages> items = parent.getItemList();

        Messages item;

        long index = new Date().getTime()-1;
        while (realm.where(Messages.class).equalTo(FIELD_ID, ++index).findFirst() != null) {}
        item = realm.createObject(Messages.class, index);
        items.add(item);
        return item;
    }

    static void delete(Realm realm, long id) {
        Messages item = realm.where(Messages.class).equalTo(FIELD_ID, id).findFirst();
        if (item != null) {
            item.deleteFromRealm();
        }
    }

}
