package com.shifu.user.twitter_project;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import static com.shifu.user.twitter_project.Messages.FIELD_ID;

public class RealmController {

    private Realm realm;
    private Context context;

    public RealmController(Context context) {
        Realm.init(context);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        realm.setDefaultConfiguration(config);
        realm = Realm.getInstance(config);

        this.context = context;
    }

    public void Clear() {
        realm.beginTransaction();
        realm.where(Messages.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

//    public void addInfo(final Countries data) {
//        realm.beginTransaction();
//        for (com.shifu.user.project1.CountriesResponse obj : data.getResponse()) {
//            Messages item = Messages.create(realm);
//                    String country_content = context.getResources().getString(R.string.country_entry,
//                            obj.getRegion(),
//                            obj.getSubRegion(),
//                            obj.getNativeLanguage(),
//                            obj.getCurrencyName());
//                    item.setTitle(obj.getName());
//                    item.setContent(country_content);
//        }
//        realm.commitTransaction();
//    }

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