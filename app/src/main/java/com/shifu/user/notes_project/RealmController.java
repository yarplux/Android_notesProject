package com.shifu.user.notes_project;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.shifu.user.notes_project.json.JsonMsg;
import com.shifu.user.notes_project.json.JsonResponse;
import com.shifu.user.notes_project.realm.Messages;
import com.shifu.user.notes_project.realm.MessagesAuthor;
import com.shifu.user.notes_project.realm.MessagesUsers;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

import java.util.Map;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

import static com.shifu.user.notes_project.realm.Messages.FIELD_ID;

import static com.shifu.user.notes_project.FirebaseController.*;

public class RealmController {

    private final static String CLASS_TAG = "RC.";
    private Realm realm;
    RealmController(Context context) {
        Realm.init(context);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        realm = Realm.getInstance(config);
        //realm = Realm.getDefaultInstance();
    }



    /**
     * CREATE DATA FUNCTIONS _______________________________________________________________________
    */

    public void addMsgs(final Map<String, JsonMsg> data, final Handler h) {
        final String TAG = CLASS_TAG+"addMsgs";
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NotNull Realm realm) {
                realm.where(Messages.class).findAll().deleteAllFromRealm();
                for (String key : data.keySet()) {
                    JsonMsg obj = data.get(key);
                    Messages item = realm.createObject(Messages.class, UUID.randomUUID().toString());
                    item.setText(obj.getText());
                    Log.d(TAG, "Created text: "+obj.getText());
                    item.setDate(obj.getDate());
                    item.setUid(obj.getUid());
                    item.setFirebase_id(key);
                }

                RealmResults<Messages> msgs = realm.where(Messages.class).findAll().sort("date");
                ActivityMain.setRA(new RealmRVAdapter(msgs));
                ActivityMain.getRA().notifyDataSetChanged();
                h.sendMessage(Message.obtain(h, 1, TAG));
            }
        });
    }

    public void addMsg(final String text, final Long date, final Handler h) {
        final String TAG = CLASS_TAG+"addMsg";
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NotNull Realm realm) {
                MessagesAuthor user = realm.where(MessagesAuthor.class).findFirst();
                Messages item = realm.createObject(Messages.class, UUID.randomUUID().toString());
                item.setText(text);
                item.setDate(date);
                item.setUid(user.getUid());
                item.setFirebase_id(null);
                ActivityMain.getRA().notifyDataSetChanged();

                Bundle obj = new Bundle();
                obj.putString("text", text);
                obj.putLong("date", date);
                obj.putString("uuid", item.getID());
                obj.putString("uid", user.getUid());
                obj.putString("idToken", user.getIdToken());
                obj.putString("refreshToken", user.getRefreshToken());

                FirebaseController.pushMsg(obj, h);
                h.sendMessage(Message.obtain(h, 1, TAG));
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

    /**
     * READ DATA FUNCTIONS _________________________________________________________________________
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

    /**
     * UPDATE DATA FUNCTIONS _______________________________________________________________________
    */

    /**
     *
     * @param obj - username, uid, idToken, refreshToken
     * @param h - handler
     */
    public void changeUserName(final Bundle obj, final Handler h) {
        final String TAG = "RC.changeUserName";
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                MessagesAuthor item = realm.where(MessagesAuthor.class).findFirst();
                item.setUsername(obj.getString("username"));
                Log.d(TAG, "Set uname: "+item.getUsername());
                RealmResults<Messages> msgs = realm.where(Messages.class).findAll().sort("date");
                ActivityMain.setRA(new RealmRVAdapter(msgs, item.getUsername()));
                ActivityMain.getRA().notifyDataSetChanged();
//                realm.where(MessagesAuthor.class).findAll().deleteAllFromRealm();
//                MessagesAuthor item = realm.createObject(MessagesAuthor.class, obj.getString("uid"));
//                item.setUid(obj.getString("username"));
//                item.setIdToken(obj.getString("idToken"));
//                item.setRefreshToken(obj.getString("refreshToken"));

                h.sendMessage(Message.obtain(h, 1, TAG));
            }
        });
    }

    /**
     *
     * @param obj - Msg: firebase_id, uuid
     * @param h - handler
     */
    public void setMsgFid(final Bundle obj, final Handler h) {
        final String TAG = "RC.setMsgFid";
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Messages item = realm.where(Messages.class).equalTo(Messages.FIELD_ID, obj.getString("uuid")).findFirst();
                item.setFirebase_id(obj.getString("firebase_id"));
                Log.d(TAG, "Success for:"+item.getText());
            }
        });
    }

    /**
     *
     * @param obj - idToken
     * @param h - handler
     */
    public void changeToken(final Bundle obj, Handler h) {
        final  String TAG = "RC.changeToken";
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                MessagesAuthor item = realm.where(MessagesAuthor.class).findFirst();
                item.setIdToken(obj.getString("idToken"));
                Log.d(TAG, "Success for:"+item.getUsername());
            }
        });
    }


    /**
     *
     * @param obj - username, uid, idToken, refreshToken
     * @param h
     */
    public void changeUser(final Bundle obj, final Handler h) {
        final String TAG = "RC.changeUser";
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NotNull Realm realm) {
                realm.where(MessagesAuthor.class).findAll().deleteAllFromRealm();
                MessagesAuthor item = realm.createObject(MessagesAuthor.class, obj.getString("uid"));
                item.setUsername(obj.getString("username"));
                item.setIdToken(obj.getString("idToken"));
                item.setRefreshToken(obj.getString("refreshToken"));
                Log.d(TAG, "Set user: "+item.toString());

                Bundle obj = new Bundle();
                obj.putString("uid", item.getUid());
                obj.putString("username", item.getUsername());
                obj.putString("idToken", item.getIdToken());
                obj.putString("refreshToken", item.getRefreshToken());

                FirebaseController.loadMsgs(obj, h);
                h.sendMessage(Message.obtain(h, 1, TAG));
            }
        });
    }

    public void refreshUser(final String source, final Bundle arg, final Handler h) {
        final String TAG = "RC.refreshUser";
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NotNull Realm realm) {
                MessagesAuthor item = realm.where(MessagesAuthor.class).findFirst();
                item.setIdToken(arg.getString("idToken"));
                item.setRefreshToken(arg.getString("refreshToken"));
                Log.d(TAG, "From:"+source);
                switch (source.split("\\.")[1]){

                    case "loadMsgs": loadMsgs(arg, h);
                        break;
                    case "delMsg": delMsg(arg, h);
                        break;
                    case "pushMsg": pushMsg(arg, h);
                        break;
                    case "pushUser": pushUser(arg, h);
                        break;
                    case "updateMsg": updateMsg(arg, h);
                        break;
                    case "updateName": updateName(arg, h);
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
                Messages item = realm.where(Messages.class).equalTo(FIELD_ID, id).findFirst();
                MessagesAuthor user = realm.where(MessagesAuthor.class).findFirst();
                try {
                    item.setText(text);
                    ActivityMain.getRA().notifyDataSetChanged();

                    Bundle obj = new Bundle();
                    obj.putString("text", text);
                    obj.putLong("date", item.getDate());
                    obj.putString("firebase_id", item.getFirebase_id());
                    obj.putString("uid", user.getUid());
                    obj.putString("idToken", user.getIdToken());
                    obj.putString("refreshToken", user.getRefreshToken());

                    //TODO если будет обработка обрывов связи, то нужно как-то проверить, существует ли это сообщение в базе
                    if (item.getFirebase_id() != null) {
                        FirebaseController.updateMsg(obj, h);
                    }
                } catch (NullPointerException e) {
                    h.sendMessage(Message.obtain(h, 0, TAG+":"+e.toString()));
                }
            }
        });
    }

    /**
     * DELETE DATA FUNCTIONS _______________________________________________________________________
     */

    public void clear (final Handler h) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NotNull Realm realm) {
                realm.where(Messages.class).findAll().deleteAllFromRealm();
                realm.where(MessagesAuthor.class).findAll().deleteAllFromRealm();
                realm.where(MessagesUsers.class).findAll().deleteAllFromRealm();
                h.sendMessage(Message.obtain(h, 1, "RC.clear"));

            }
        });
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
                        ActivityMain.getRA().notifyDataSetChanged();

                        //TODO если будет обработка обрывов связи, то нужно как-то проверить, существует ли это сообщение в базе
                        if (firebase_id != null) {
                            MessagesAuthor user = realm.where(MessagesAuthor.class).findFirst();
                            Bundle obj = new Bundle();
                            obj.putString("firebase_id", firebase_id);
                            obj.putString("idToken", user.getIdToken());
                            obj.putString("refreshToken", user.getRefreshToken());
                            FirebaseController.delMsg(obj, h);
                        }
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                    h.sendMessage(Message.obtain(h, 0, TAG));
                }
            }
        });
    }


}