package com.shifu.user.twitter_project;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import static com.shifu.user.twitter_project.Messages.FIELD_ID;

public class RealmController {

    private Realm realm;
    private Context context;

    public RealmController(Context context) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();

        this.context = context;
    }

    public void Clear() {
        realm.beginTransaction();
        realm.where(Messages.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

    public void addInfo(final JsonList data) {
        realm.beginTransaction();
        for (JsonItem obj : data.getJsonItems()) {
            Messages item = Messages.create(realm);
                    String content = context.getResources().getString(R.string.article_entry,
                            obj.getTitle(),
                            obj.getUrl());
                    item.setText(content);
        }
        realm.commitTransaction();
    }

    public void addInfo(final DataSnapshot data) {
        realm.beginTransaction();
        for (DataSnapshot postSnapshot : data.getChildren()) {
            HashMap<String, Object> item = (HashMap<String, Object>) postSnapshot.getValue();
            Messages obj = Messages.create(realm, (Long) item.get("date"));
            obj.setText((String)item.get("text"));
        }
        realm.commitTransaction();
    }


    public void addInfo(final String text) {
        if (text == null || text.equals("")) return;
        realm.beginTransaction();
        Messages obj = Messages.create(realm);
        obj.setText(text);
        realm.commitTransaction();
    }

    public RealmResults<Messages> getInfo() {
        return realm.where(Messages.class).findAll();
    }

    public void updateInfo(final Long id, final String text) {
        if (text == null || text.equals("")) return;
        realm.beginTransaction();
        Messages obj = realm.where(Messages.class).equalTo(FIELD_ID, id).findFirst();
        obj.setText(text);
        realm.commitTransaction();
    }

    public void removeItemById(final long id) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Messages.delete(realm, id);
            }
        });
    }

}