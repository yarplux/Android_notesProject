package com.shifu.user.twitter_project;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Map;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirebaseController{

    private Handler h;
    private JsonApi jsonApi;
    private Context context;
    private RealmRVAdapter mAdapter;

//    private RealmRVAdapterUsers uAdapter;

    private final static String FIREBASE_URL = "https://shifu-ad6cd.firebaseio.com/";
    private final static String API_KEY = "AIzaSyAfOcB4p-ewv5LQhtNGRRlcg3_S8Iip-CY";
    private final static String MAIL_DOMAIN = "@mail.ru";

    FirebaseController (Context context, RealmRVAdapter mAdapter) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FIREBASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        jsonApi = retrofit.create(JsonApi.class);
        this.mAdapter = mAdapter;
        this.context = context;
    }

    FirebaseController (String baseUrl, Handler h) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        jsonApi = retrofit.create(JsonApi.class);
        this.h = h;
    }

    public void signIn(String user, String pass) {
        String mail = user+MAIL_DOMAIN;
        Log.d("GET TOKEN "+mail, jsonApi.signIn("application/json", API_KEY, new JsonSignInUpRequest(mail, pass)).request().toString());
        jsonApi.signIn("application/json", API_KEY, new JsonSignInUpRequest(mail, pass)).enqueue(new Callback<JsonSignInUpResponse>() {
            @Override
            public void onResponse(@NotNull Call <JsonSignInUpResponse> call, @NotNull Response <JsonSignInUpResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("GET TOKEN RESPONSE", response.body().toString());
                    h.sendEmptyMessage(1);
                } else {
                    Message msg = new Message();
                    try {
                        // Мы знаем структуру получаемого JSON.
                        // Нужно ли переделывать этот способ на десериализацию? / конвертацию?
                        JSONObject jAllErrors = new JSONObject(response.errorBody().string());
                        JSONObject jObjError = new JSONObject(jAllErrors.get("error").toString());

                        Log.e("GET error", jObjError.getString("message"));

                        msg.obj = jObjError.getString("message");
                        msg.what = 0;
                        h.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                        msg.obj = e.toString();
                        msg.what = 0;
                        h.sendMessage(msg);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonSignInUpResponse> call, @NotNull Throwable t) {
                Message msg = new Message();
                Log.e("GET Failure", call.toString());
                msg.obj = t.toString();
                msg.what = 0;
                h.sendMessage(msg);
            }

        });

    }

    public void signUp(final String user, String pass) {
        String mail = user+MAIL_DOMAIN;
        Log.d("GET TOKEN", jsonApi.signUp("application/json", API_KEY, new JsonSignInUpRequest(mail,pass)).request().toString());
        jsonApi.signUp("application/json", API_KEY, new JsonSignInUpRequest(mail,pass)).enqueue(new Callback<JsonSignInUpResponse>() {
            @Override
            public void onResponse(@NotNull Call<JsonSignInUpResponse> call, @NotNull Response<JsonSignInUpResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("GET TOKEN RESPONSE", response.body().toString());
                        updateProfile(user, response.body().getLocalId(), response.body().getIdToken());
                } else {
                    Message msg = new Message();
                    try {
                        // Мы знаем структуру получаемого JSON.
                        // Нужно ли переделывать этот способ на десериализацию? / конвертацию?
                        JSONObject jAllErrors = new JSONObject(response.errorBody().string());
                        JSONObject jObjError = new JSONObject(jAllErrors.get("error").toString());

                        Log.e("GET error", jObjError.getString("message"));
                        msg.obj = jObjError.getString("message");
                        msg.what = 0;
                        h.sendMessage(msg);
                    } catch (Exception e){
                        e.printStackTrace();
                        msg.obj = e.toString();
                        msg.what = 0;
                        h.sendMessage(msg);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonSignInUpResponse> call, @NotNull Throwable t) {
                Message msg = new Message();
                Log.e("GET Failure", call.toString());
                msg.obj = t.toString();
                msg.what = 0;
                h.sendMessage(msg);
            }
        });
    }

    public void updateProfile(final String username, final String uid, final String idToken) {

        Log.d("UPDATE", jsonApi
                .updateProfile("application/json", API_KEY, new JsonUpdateAuthRequest(idToken,username, true))
                .request()
                .toString());

        jsonApi.updateProfile("application/json", API_KEY, new JsonUpdateAuthRequest(idToken,username, true))
                .enqueue(new Callback<JsonUpdateAuthResponse>() {

            @Override
            public void onResponse(@NotNull Call<JsonUpdateAuthResponse> call, @NotNull Response<JsonUpdateAuthResponse> response) {
                if (response.isSuccessful()) {

                    Log.d("UPDATE LocalID ?", (uid.equals(response.body().getLocalId()))?"same":"new");
                    Log.d("UPDATE idToken ?", (idToken.equals(response.body().getIdToken()))?"same":"new");
                    Log.d("UPDATE Expires in", response.body().getExpiresIn()+" sec");

                    Message msg = new Message();
                    msg.obj = response.body().getDisplayName();
                    msg.what = 1;
                    h.sendMessage(msg);

                } else {
                    Message msg = new Message();
                    try {
                        // Мы знаем структуру получаемого JSON.
                        // Нужно ли переделывать этот способ на десериализацию? / конвертацию?
                        JSONObject jAllErrors = new JSONObject(response.errorBody().string());
                        JSONObject jObjError = new JSONObject(jAllErrors.get("error").toString());

                        Log.e("GET error", jObjError.getString("message"));
                        msg.obj = jObjError.getString("message");
                        msg.what = 0;
                        h.sendMessage(msg);
                    } catch (Exception e){
                        e.printStackTrace();
                        msg.obj = e.toString();
                        msg.what = 0;
                        h.sendMessage(msg);
                    }

                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonUpdateAuthResponse> call, @NotNull Throwable t) {
                Message msg = new Message();
                Log.e("GET Failure", call.toString());
                msg.obj = t.toString();
                msg.what = 0;
                h.sendMessage(msg);
            }
        });
    }

//    public void pushUser(final String username, final String uid) {
//        jsonApi.pushUser(new JsonResponse(username)).enqueue(new Callback<JsonResponse>() {
//
//            @Override
//            public void onResponse(@NotNull Call<JsonResponse> call, @NotNull Response<JsonResponse> response) {
//                if (response.isSuccessful()) {
//                    RealmController rc = new RealmController(context);
//                    rc.delegate = delegate;
//                    rc.changeUser(username);
//                    Log.d("USER POST Success", response.body().toString());
//                } else {
//                    Log.d("USER POST Fail", response.body().toString());
//                    delegate.loadFinish(context.getResources().getString(R.string.post_error), false);
//                }
//            }
//            @Override
//            public void onFailure(@NotNull Call<JsonResponse> call, @NotNull Throwable t) {
//                Log.e("POST Error", t.toString());
//                delegate.loadFinish(context.getResources().getString(R.string.post_error), false);
//            }
//        });
//    }
//
//    public void loadUsers(final String username, final Handler h) {
//        Log.d("GET URL", jsonApi.loadUsers().request().url().toString());
//        jsonApi.loadUsers().enqueue(new Callback<Map<String, JsonResponse>>() {
//            @Override
//            public void onResponse(@NotNull Call<Map<String, JsonResponse>> call, @NotNull Response<Map<String, JsonResponse>> response) {
//                if (response.isSuccessful()) {
//                    Log.d("GET USERS success!", response.body().toString());
//                    Map<String, JsonResponse> data = response.body();
//                    data.remove(username);
//
//                    RealmController rc = new RealmController(context);
//                    rc.addUsers(data, h);
//                } else {
//                    Log.e("GET error", response.errorBody().toString());
//                    h.sendEmptyMessage(0);
//                }
//            }
//
//            @Override
//            public void onFailure(@NotNull Call<Map<String, JsonResponse>> call, @NotNull Throwable t) {
//                t.printStackTrace();
//                h.sendEmptyMessage(0);
//            }
//        });
//    }
//
//    public void checkUser(final String username) {
//        Log.d("GET URL", jsonApi.checkAuthor("\"name\"", "\""+username+"\"").request().url().toString());
//        jsonApi.checkAuthor("\"name\"", "\""+username+"\"").enqueue(new Callback<Map<String, JsonResponse>>() {
//            @Override
//            public void onResponse(@NotNull Call<Map<String, JsonResponse>> call, @NotNull Response<Map<String, JsonResponse>> response) {
//                if (response.isSuccessful()) {
//                    if (response.body().isEmpty()) {
//                        Log.d("GET USERS", username+" - новый пользователь");
//                        pushUser(username);
//                    } else {
//                        Log.d("GET USERS", "Поользователь "+username+" уже существует!");
//                        delegate.loadFinish("", true);
//                        //delegate.loadFinish(context.getResources().getString(R.string.exist), false);
//                    }
//                } else {
//                    Log.e("GET error", response.errorBody().toString());
//                    delegate.loadFinish(context.getResources().getString(R.string.get_error), false);
//                }
//            }
//
//            @Override
//            public void onFailure(@NotNull Call<Map<String, JsonResponse>> call, @NotNull Throwable t) {
//                t.printStackTrace();
//                delegate.loadFinish(context.getResources().getString(R.string.get_error), false);
//            }
//        });
//    }
//
//    public void pushUser(final String username) {
//        jsonApi.pushUser(new JsonResponse(username)).enqueue(new Callback<JsonResponse>() {
//
//            @Override
//            public void onResponse(@NotNull Call<JsonResponse> call, @NotNull Response<JsonResponse> response) {
//                if (response.isSuccessful()) {
//                    RealmController rc = new RealmController(context);
//                    rc.delegate = delegate;
//                    rc.changeUser(username);
//                    Log.d("USER POST Success", response.body().toString());
//                } else {
//                    Log.d("USER POST Fail", response.body().toString());
//                    delegate.loadFinish(context.getResources().getString(R.string.post_error), false);
//                }
//            }
//            @Override
//            public void onFailure(@NotNull Call<JsonResponse> call, @NotNull Throwable t) {
//                Log.e("POST Error", t.toString());
//                delegate.loadFinish(context.getResources().getString(R.string.post_error), false);
//            }
//        });
//    }

    public void loadMessages(String username) {
        Log.d("GET URL", jsonApi.loadMessages("\"author\"", "\""+username+"\"").request().url().toString());
        jsonApi.loadMessages("\"author\"", "\""+username+"\"").enqueue(new Callback<Map<String, JsonItem>>() {
            @Override
            public void onResponse(@NotNull Call<Map<String, JsonItem>> call, @NotNull Response<Map<String, JsonItem>> response) {
                if (response.isSuccessful()) {
                    Log.d("GET MSGS success!", response.body().toString());
                    new RealmController(context, h).addDBInfo(response.body());
                    mAdapter.notifyDataSetChanged();
                } else {
                    Log.e("GET error", response.errorBody().toString());
                }
            }
            @Override
            public void onFailure(@NotNull Call<Map<String, JsonItem>> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
    }


    public void pushMessage(final String text, final Long date, String username) {

        jsonApi.pushMessage(new JsonItem(text,date, username)).enqueue(new Callback<JsonResponse>() {

            @Override
            public void onResponse(@NotNull Call<JsonResponse> call, @NotNull Response<JsonResponse> response) {

                if (response.isSuccessful()) {
                    RealmController rc = new RealmController(context, h);
                    rc.addInfo(text, date, response.body().getName());

                    // DANGEROUS OPERATION, IF DB TOO MUCH!
                    int number = (int)(long) rc.getDatabaseSize();
                    mAdapter.notifyItemInserted(number);

                    Log.d("POST Success", response.body().toString());
                } else {
                    Log.d("POST Fail", response.body().toString());
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonResponse> call, @NotNull Throwable t) {
                Log.e("POST Error", t.toString());
            }
        });
    }

    public void deleteMessage(final int position) {

        //Log.d("Deleted:", Long.toString(mAdapter.getItem(position).getID()));
        new RealmController(context, h).removeItemById(mAdapter.getItem(position).getID());
        mAdapter.notifyDataSetChanged();

        jsonApi.deleteMessage(mAdapter.getItem(position).getFirebase_id())
                .enqueue(new Callback<JsonResponse>() {

            @Override
            public void onResponse(@NotNull Call<JsonResponse> call, @NotNull Response<JsonResponse> response) {
            }
            @Override
            public void onFailure(@NotNull Call<JsonResponse> call, @NotNull Throwable t) {
                Log.e("DELETE Error", t.toString());
            }
        });

    }

    public void updateMessage(int position, final String text) {

        new RealmController(context, h).updateInfo(mAdapter.getItem(position).getID(), text);
        mAdapter.notifyDataSetChanged();

        JsonItem jsonItem = new JsonItem();
        jsonItem.setText(text);
        jsonItem.setDate(mAdapter.getItem(position).getDate());
        jsonItem.setAuthor(mAdapter.getItem(position).getUsername());
        jsonItem.setRetwitted(mAdapter.getItem(position).getRetwitted());
        jsonApi.putMessage(mAdapter.getItem(position).getFirebase_id(), jsonItem)
                .enqueue(new Callback<JsonItem>() {

            @Override
            public void onResponse(@NotNull Call<JsonItem> call, @NotNull Response<JsonItem> response) {

                if (response.isSuccessful()) {
                    Log.d("POST Success", response.body().toString());
                } else {
                    Log.d("POST Fail", response.body().toString());
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonItem> call, @NotNull Throwable t) {
                Log.e("POST Error", t.toString());
            }
        });

    }
}
