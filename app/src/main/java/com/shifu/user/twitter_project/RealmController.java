package com.shifu.user.twitter_project;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

import static com.shifu.user.twitter_project.Messages.FIELD_ID;

public class RealmController {

    private Realm realm;
    RealmController(Context context) {
        Realm.init(context);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        realm = Realm.getInstance(config);
        //realm = Realm.getDefaultInstance();
    }

    /*
    CREATE DATA FUNCTIONS __________________________________________________________________________
    */

    public void addMsgs(final Map<String, JsonMsg> data, final String author, final Handler h) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NotNull Realm realm) {
                realm.where(Messages.class).findAll().deleteAllFromRealm();
                for (String key : data.keySet()) {
                    JsonMsg obj = data.get(key);
                    Messages item = realm.createObject(Messages.class, UUID.randomUUID().toString());
                    item.setText(obj.getText());
                    item.setDate(obj.getDate());
                    item.setUsername(author);
                    item.setRetwitted(obj.getRetwitUid());
                    item.setFirebase_id(key);
                }
                h.sendMessage(Message.obtain(h, 1, "RC.addMsgs"));
            }
        });
    }

    public void addMsg(final String text, final Long date, final Handler h) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NotNull Realm realm) {
                Messages item = realm.createObject(Messages.class, UUID.randomUUID().toString());
                item.setText(text);
                item.setDate(date);
                item.setUsername(realm.where(MessagesAuthor.class).findFirst().getUsername());
                item.setRetwitted(item.getRetwitted());
                item.setFirebase_id(item.getFirebase_id());
                h.sendMessage(Message.obtain(h, 4, item.getID()));
            }
        });
    }

    public void addUsers(final Map<String, JsonResponse> data, final Handler h) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NotNull Realm realm) {
                realm.where(MessagesUsers.class).findAll().deleteAllFromRealm();
                for (String key: data.keySet()) {
                    MessagesUsers item = realm.createObject(MessagesUsers.class, key);
                    item.setUsername(data.get(key).getName());
                }
                h.sendMessage(Message.obtain(h, 1, "RC.addUsers"));
            }
        });
    }

    /*
    READ DATA FUNCTIONS ____________________________________________________________________________
     */

    public <T extends RealmObject> Long getSize (Class<T> objClass) {
        if (objClass == null) return null;
        return realm.where(objClass).count();
    }

    public <T extends RealmObject> RealmResults<T> getBase(Class<T> objClass, String sortField){
        RealmResults<T> base;
        final String TAG = "RC.getBase";
        boolean sort = false;
        if (sortField != null)
        {
            for (Field f: Messages.class.getDeclaredFields()) {
                if (f.getName().equals(sortField)){
                    sort = true;
                    break;
                }
            }
        }

        if (sort) {
            base = realm.where(objClass).findAll().sort(sortField);
        } else {
            base = realm.where(objClass).findAll();
        }
        return base;
    }

    public <T extends RealmObject> T getItem(Class<T> objClass, String field, Object value){
        if (objClass == null) return null;
        boolean has = false;
        if (field != null && value != null)
        {
            for (Field f: Messages.class.getDeclaredFields()) {
                if (f.getName().equals(field)){
                    has = true;
                    break;
                }
            }
        }
        T item;
        if (has){
            if (value instanceof String) {
                item = realm.where(objClass).equalTo(field, (String) value).findFirst();
            } else if (value instanceof Long) {
                item = realm.where(objClass).equalTo(field, (Long) value).findFirst();
            } else if (value instanceof Integer) {
                item = realm.where(objClass).equalTo(field, (Integer) value).findFirst();
            } else if (value instanceof Boolean) {
                item = realm.where(objClass).equalTo(field, (Boolean) value).findFirst();
            } else {
                item = null;
            }
        } else {
            item = realm.where(objClass).findFirst();
        }
        return item;
    }

    /*
    UPDATE DATA FUNCTIONS __________________________________________________________________________
    */

    public void changeUser(final Auth auth, final Handler h) {
        final String TAG = "RC.changeUser";
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NotNull Realm realm) {
                MessagesAuthor item = realm.createObject(MessagesAuthor.class, auth.getUid());
                item.setUsername(auth.getUsername());
                item.setIdToken(auth.getIdToken());
                item.setRefreshToken(auth.getRefresh());
                h.sendMessage(Message.obtain(h, 1, TAG));
            }
        });
    }

    public void refreshUser(final String idToken, final String refreshToken, final String source, final Object arg, final Handler h) {
        final String TAG = "RC.refreshUser";
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NotNull Realm realm) {
                MessagesAuthor item = realm.where(MessagesAuthor.class).findFirst();
                item.setIdToken(idToken);
                item.setRefreshToken(refreshToken);
                FirebaseController fc = new FirebaseController(ActivityMain.URL_DATABASE, h);
                switch (source.split(".")[1]){
                    case "loadMsgs":
                        fc.loadMsgs();
                        break;
                    case "delMsg":
                        fc.delMsg((String) arg);
                        break;
                    case "pushMsg":
                        fc.pushMsg((String) arg);
                        break;
                    case "pushUser":
                        fc.pushUser((Auth) arg);
                        break;
                    case "updateMsg":
                        fc.updateMsg((String) arg);
                        break;
                }
                h.sendMessage(Message.obtain(h, 1, TAG));
            }
        });
    }

    public void changeMsg(final String id, final String text, final Handler h) {
        final String TAG = "RC.changeMsg";
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NotNull Realm realm) {
                Messages obj = realm.where(Messages.class).equalTo(FIELD_ID, id).findFirst();
                try {
                    obj.setText(text);
                    h.sendMessage(Message.obtain(h, 1, TAG));
                    h.sendMessage(Message.obtain(h, 6, id));
                } catch (NullPointerException e) {
                    h.sendMessage(Message.obtain(h, 0, TAG+":"+e.toString()));
                }
            }
        });
    }

    /*
    DELETE DATA FUNCTIONS __________________________________________________________________________
     */

    public <T extends RealmObject> void clear (final Class<T> objClass, final Handler h) {
        if (objClass == null) return;
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NotNull Realm realm) {
                realm.where(objClass).findAll().deleteAllFromRealm();
            }
        });
        h.sendMessage(Message.obtain(h, 1, "RC.clear"));
    }

    public <T extends RealmObject> void removeItemById(final Class<T> objClass, final String id, final Handler h) {
        if (objClass == null) return;
        final String TAG = "RC.removeItemById";
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NotNull Realm realm) {
                try {
                    T item = realm.where(objClass).equalTo((String)objClass.getField("FIELD_ID").get(null), id).findFirst();
                    if (item != null) {
                        String firebase_id=null;
                        //TODO если буду делать удаление из таблицы юзеров - писать сюда
                        if (item instanceof Messages) {
                            firebase_id = ((Messages) item).getFirebase_id();
                        }
                        item.deleteFromRealm();
                        h.sendMessage(Message.obtain(h, 5,   firebase_id));
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                    h.sendMessage(Message.obtain(h, 0, TAG));
                }
            }
        });
    }

    protected void destroy() {
        realm.close();
        realm = null;
    }

}