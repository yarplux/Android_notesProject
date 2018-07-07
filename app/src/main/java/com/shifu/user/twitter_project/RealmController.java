package com.shifu.user.twitter_project;

import android.content.Context;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import static com.shifu.user.twitter_project.Messages.FIELD_ID;

public class RealmController {

    private Realm realm;
    private Context context;

    RealmController(Context context) {
        Realm.init(context);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        realm = Realm.getInstance(config);
        //realm = Realm.getDefaultInstance();
        this.context = context;
    }

    public void addDBInfo(Map<String, JsonItem> data) {
        realm.beginTransaction();
        realm.where(Messages.class).findAll().deleteAllFromRealm();
        for (String key: data.keySet()) {
            JsonItem obj = data.get(key);
            Messages item = Messages.create(realm);
            item.setText(obj.getText());
            item.setDate(obj.getDate());
            item.setFirebase_id(key);
        }
        realm.commitTransaction();
    }

    public void addInfo(String text, long date, String firebase_id) {
        realm.beginTransaction();
        Messages obj = Messages.create(realm);
        obj.setText(text);
        obj.setDate(date);
        obj.setFirebase_id(firebase_id);
        realm.commitTransaction();
    }

    public void updateInfo(final String id, final String text) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NotNull Realm realm) {
                Messages obj = realm.where(Messages.class).equalTo(FIELD_ID, id).findFirst();
                obj.setText(text);
            }
        });
    }

    public void removeItemById(final String id) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NotNull Realm realm) {
                Messages.delete(realm, id);
            }
        });
    }

    public RealmResults<Messages> getInfo() {
        return realm.where(Messages.class).findAll();
    }

    public Long getDatabaseSize() { return realm.where(Messages.class).count(); }

}