package com.shifu.user.twitter_project;

import android.os.Message;
import android.util.Log;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Map;


import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Callback;


public class FirebaseController {

    private Handler h;
    private JsonApi jsonApi;


    private final static String API_KEY = "AIzaSyAfOcB4p-ewv5LQhtNGRRlcg3_S8Iip-CY";
    private final static String MAIL_DOMAIN = "@mail.ru";
    private final static String ERROR_TOKEN_EXPIRED = "Auth token is expired";
    private final static String ERROR_TOKEN_INVALID = "Could not parse auth token";

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

    public void login(final String user, String pass, final Boolean newUser) {

        String mail = user+MAIL_DOMAIN;
        String action = (newUser)?"signupNewUser":"verifyPassword";
        final String TAG = "FC.login";

        Log.d(TAG, jsonApi.login(action, "application/json", API_KEY, new JsonLoginRequest(mail,pass)).request().toString());
        jsonApi.login(action,"application/json", API_KEY, new JsonLoginRequest(mail,pass)).enqueue(new Callback<JsonLoginResponse>() {
            @Override
            public void onResponse(@NotNull Call<JsonLoginResponse> call, @NotNull Response<JsonLoginResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, response.body().toString());
                    if (newUser) {
                        updateProfile(user, response.body().getLocalId(), response.body().getIdToken());
                    } else {
                        h.sendMessage(Message.obtain(h, 2, new Auth(user, response.body().getLocalId(), response.body().getIdToken(), response.body().getRefreshToken())));
                    }
                } else {
                    try {
                        ResponseError(TAG, new JSONObject(response.errorBody().string()));
                    } catch (Exception e) {
                        ResponseUnknownError(TAG, e);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonLoginResponse> call, @NotNull Throwable t) {
                Failure(TAG, t);
            }
        });
    }

    public void updateProfile(final String username, final String uid, final String idToken) {

        final String TAG = "FC.updateProfile";
        Log.d(TAG, jsonApi
                .updateProfile("application/json", API_KEY, new JsonUpdateAuthRequest(idToken,username, false))
                .request()
                .toString());

        jsonApi.updateProfile("application/json", API_KEY, new JsonUpdateAuthRequest(idToken,username, false))
                .enqueue(new Callback<JsonUpdateAuthResponse>() {

            @Override
            public void onResponse(@NotNull Call<JsonUpdateAuthResponse> call, @NotNull Response<JsonUpdateAuthResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Success:" + response.body().toString());
                    h.sendMessage(Message.obtain(h, 3, new Auth(username, uid, idToken, response.body().getRefreshToken())));
                } else {
                    try {
                        ResponseError(TAG, new JSONObject(response.errorBody().string()));
                    } catch (Exception e) {
                        ResponseUnknownError(TAG, e);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonUpdateAuthResponse> call, @NotNull Throwable t) {
                Failure(TAG, t);
            }
        });
    }


    private void refresh(final String source, final Object arg, final String refreshToken) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ActivityMain.URL_REFRESH)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        JsonApi jsonApi = retrofit.create(JsonApi.class);

        final String TAG = "FC.refresh";
        Log.d(TAG, jsonApi
                .refresh("application/json", API_KEY, new JsonRefreshRequest(refreshToken))
                .request()
                .toString());

        jsonApi.refresh("application/json", API_KEY, new JsonRefreshRequest(refreshToken))
                .enqueue(new Callback<JsonRefreshResponse>() {

                    @Override
                    public void onResponse(@NotNull Call<JsonRefreshResponse> call, @NotNull Response<JsonRefreshResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "Success:" + response.body().toString());

                            ActivityMain.getRC().refreshUser(
                                    response.body().getIdToken(),
                                    response.body().getRefreshToken(),
                                    source,
                                    arg,
                                    h);
                        } else {
                            try {
                                String jStr = response.errorBody().string();
                                Log.d(TAG, jStr);
                                jStr = jStr.replaceAll("\\\\", "");
                                JSONObject jObj = new JSONObject(jStr);

                            } catch (Exception e) {
                                ResponseUnknownError(TAG, e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<JsonRefreshResponse> call, @NotNull Throwable t) {
                        Failure(TAG, t);
                    }
                });
    }

    // TODO: при подключении учесть создание событий в handler и обработку в функции, куда передаются ошибки
    public void loadUsers(final Auth auth) {
        final String TAG = "FC.loadUsers";
        Log.d(TAG,  jsonApi.loadUsers(auth.getIdToken()).request().toString());
        jsonApi.loadUsers(auth.getIdToken()).enqueue(new Callback<Map<String, JsonResponse>>() {
            @Override
            public void onResponse(@NotNull Call<Map<String, JsonResponse>> call, @NotNull Response<Map<String, JsonResponse>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG+" Success!", response.body().toString());
                    Map<String, JsonResponse> data = response.body();
                    data.remove(auth.getUid());
                    ActivityMain.getRC().addUsers(data, h);
                } else {
                    Log.e(TAG+" Err", response.toString());
                    h.sendMessage(Message.obtain(h, 0, response.errorBody().toString()));
                }
            }

            @Override
            public void onFailure(@NotNull Call<Map<String, JsonResponse>> call, @NotNull Throwable t) {
                Failure(TAG, t);
            }
        });
    }

    public void pushUser(final Auth auth) {
        final String TAG = "FC.pushUser";
        Log.d(TAG, jsonApi.pushUser(auth.getUid(), new JsonResponse(auth.getUsername()), auth.getIdToken()).request().toString());
        jsonApi.pushUser(auth.getUid(), new JsonResponse(auth.getUsername()), auth.getIdToken())
                .enqueue(new Callback<JsonResponse>() {

            @Override
            public void onResponse(@NotNull Call<JsonResponse> call, @NotNull Response<JsonResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG+" Success", response.body().toString());
                    h.sendMessage(Message.obtain(h, 2, auth));
                } else {
                    try {
                        JSONObject jObj = new JSONObject(response.errorBody().string().replaceAll("\\\\", ""));
                        Log.d(TAG, jObj.getString("error"));
                        if (jObj.getString("error").equals(ERROR_TOKEN_EXPIRED)) {
                            refresh(TAG, auth, auth.getRefresh());
                        }
                    } catch (Exception e) {
                        ResponseUnknownError(TAG, e);
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call<JsonResponse> call, @NotNull Throwable t) {
                Failure(TAG, t);
            }
        });
    }


    public void loadMsgs(final Auth auth) {
        final String TAG = "FC.loadMsgs";
        String FIELD = "\"author\"";
        Log.d(TAG, jsonApi.loadMessages(FIELD, "\""+auth.getUid()+"\"", auth.getIdToken()).request().url().toString());
        jsonApi.loadMessages(FIELD, "\""+auth.getUid()+"\"", auth.getIdToken()).enqueue(new Callback<Map<String, JsonMsg>>() {
            @Override
            public void onResponse(@NotNull Call<Map<String, JsonMsg>> call, @NotNull Response<Map<String, JsonMsg>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG+" Success!", response.body().toString());
                    ActivityMain.getRC().addMsgs(response.body(), auth.getUsername(), h);
                    h.sendMessage(Message.obtain(h, 1, TAG));
                } else {
                    try {
                        JSONObject jObj = new JSONObject(response.errorBody().string().replaceAll("\\\\", ""));
                        Log.d(TAG, jObj.getString("error"));
                        if (jObj.getString("error").equals(ERROR_TOKEN_EXPIRED)) {
                            refresh(TAG, auth, auth.getRefresh());
                        }
                    } catch (Exception e) {
                        ResponseUnknownError(TAG, e);
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call<Map<String, JsonMsg>> call, @NotNull Throwable t) {
                Failure(TAG, t);
            }
        });
    }


    public void pushMsg(final String uuid) {
        final String TAG = "FC.pushMsg";
        final MessagesAuthor auth = ActivityMain.getRC().getItem(MessagesAuthor.class, null, null);
        Messages msg = ActivityMain.getRC().getItem(Messages.class, Messages.FIELD_ID, uuid);
        Log.d(TAG, jsonApi.pushMessage(new JsonMsg(msg.getText(),msg.getDate(), auth.getUid()), auth.getIdToken()).request().url().toString());
        jsonApi.pushMessage(new JsonMsg(msg.getText(),msg.getDate(), auth.getUid()), auth.getIdToken()).enqueue(new Callback<JsonResponse>() {

            @Override
            public void onResponse(@NotNull Call<JsonResponse> call, @NotNull Response<JsonResponse> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG+" Success!", response.body().toString());
                } else {
                    try {
                        JSONObject jObj = new JSONObject(response.errorBody().string().replaceAll("\\\\", ""));
                        Log.d(TAG, jObj.getString("error"));
                        if (jObj.getString("error").equals(ERROR_TOKEN_EXPIRED)) {
                            refresh(TAG, uuid, auth.getRefreshToken());
                        }
                    } catch (Exception e) {
                        ResponseUnknownError(TAG, e);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonResponse> call, @NotNull Throwable t) {
                Failure(TAG, t);
            }
        });
    }

    public void delMsg(final String firebase_id) {
        final String TAG = "FC.delMsg";
        final MessagesAuthor auth = ActivityMain.getRC().getItem(MessagesAuthor.class, null, null);
        Log.d(TAG, jsonApi.deleteMessage(firebase_id, auth.getIdToken()).request().url().toString());
        jsonApi.deleteMessage(firebase_id,auth.getIdToken())
                .enqueue(new Callback<JsonResponse>() {

            @Override
            public void onResponse(@NotNull Call<JsonResponse> call, @NotNull Response<JsonResponse> response) {
                if (response.isSuccessful()) {
                    h.sendMessage(Message.obtain(h, 1, TAG+":Success"));
                } else {
                    try {
                        JSONObject jObj = new JSONObject(response.errorBody().string().replaceAll("\\\\", ""));
                        h.sendMessage(Message.obtain(h, 0, TAG+":"+jObj.getString("error")));
                        if (jObj.getString("error").equals(ERROR_TOKEN_EXPIRED)) {
                            refresh(TAG, firebase_id, auth.getRefreshToken());
                        }
                    } catch (Exception e) {
                        ResponseUnknownError(TAG, e);
                    }
                }

            }
            @Override
            public void onFailure(@NotNull Call<JsonResponse> call, @NotNull Throwable t) {
                Failure(TAG, t);
            }
        });

    }

    public void updateMsg(final String uuid) {
        final String TAG = "FC.updateMsg";
        final MessagesAuthor auth = ActivityMain.getRC().getItem(MessagesAuthor.class, null, null);
        Messages msg = ActivityMain.getRC().getItem(Messages.class, Messages.FIELD_ID, uuid);
        Log.d(TAG,msg.toString());
        jsonApi.putMessage(msg.getFirebase_id(), new JsonMsg(msg.getText(),msg.getDate(), auth.getUid()), auth.getIdToken())
                .enqueue(new Callback<JsonMsg>() {

            @Override
            public void onResponse(@NotNull Call<JsonMsg> call, @NotNull Response<JsonMsg> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG+" Success", response.body().toString());
                } else {
                    try {
                        JSONObject jObj = new JSONObject(response.errorBody().string().replaceAll("\\\\", ""));
                        Log.d(TAG, jObj.getString("error"));
                        if (jObj.getString("error").equals(ERROR_TOKEN_EXPIRED)) {
                            refresh(TAG, uuid, auth.getRefreshToken());
                        }
                    } catch (Exception e) {
                        ResponseUnknownError(TAG, e);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonMsg> call, @NotNull Throwable t) {
                Failure(TAG, t);
            }
        });

    }

    private void Failure(String tag, @NotNull Throwable t) {
        Log.e(tag+ " Fail", t.toString());
        h.sendMessage(Message.obtain(h, 0, t.toString()));
    }

    private  void ResponseError(String tag, JSONObject jAllErrors) throws Exception {
            // Мы знаем структуру получаемого JSON.
            // Нужно ли переделывать этот способ на десериализацию? / конвертацию собственную?
            JSONObject jObjError = new JSONObject(jAllErrors.get("error").toString());
            String error = jObjError.getString("message");
            Log.e(tag+" Err", error);
            h.sendMessage(Message.obtain(h, 0, error));
    }

    private void ResponseUnknownError(String tag, Exception e) {
        Log.e(tag+" Exc", e.toString());
        h.sendMessage(Message.obtain(h, 0, e.toString()));
    }
}
