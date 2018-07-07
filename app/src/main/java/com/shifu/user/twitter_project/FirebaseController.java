package com.shifu.user.twitter_project;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirebaseController {

    private JsonApi jsonApi;
    private Context context;
    private RealmRVAdapter mAdapter;

    private static String result=null;

    FirebaseController (String baseUrl, Context context, RealmRVAdapter mAdapter) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        jsonApi = retrofit.create(JsonApi.class);

        this.mAdapter = mAdapter;
        this.context = context;
    }

    public void loadMessages() {

        result = null;

        jsonApi.loadMessages("messages.json").enqueue(new Callback<Map<String, JsonItem>>() {
            @Override
            public void onResponse(@NotNull Call<Map<String, JsonItem>> call, @NotNull Response<Map<String, JsonItem>> response) {
                if (response.isSuccessful()) {
                    Log.d("GET success!", response.body().toString());
                    new RealmController(context).addDBInfo(response.body());
                    mAdapter.notifyDataSetChanged();
                } else {
                    Log.e("GET error", response.errorBody().toString());
                }

                result = Boolean.toString(response.isSuccessful());
            }

            @Override
            public void onFailure(@NotNull Call<Map<String, JsonItem>> call, @NotNull Throwable t) {
                t.printStackTrace();

                result = t.toString();
            }
        });

    }


    public void pushMessage(final String text, final Long date, Long author_id) {

        result = null;
        jsonApi.pushMessage(new JsonItemPost(text,date, author_id)).enqueue(new Callback<JsonResponse>() {

            @Override
            public void onResponse(@NotNull Call<JsonResponse> call, @NotNull Response<JsonResponse> response) {

                if (response.isSuccessful()) {
                    RealmController rc = new RealmController(context);
                    rc.addInfo(text, date, response.body().name);

                    // DANGEROUS OPERATION, IF DB TOO MUCH!
                    int number = (int)(long) rc.getDatabaseSize();
                    mAdapter.notifyItemInserted(number);

                    Log.d("POST Success", response.body().toString());
                } else {
                    Log.d("POST Fail", response.body().toString());
                }
                result = Boolean.toString(response.isSuccessful());
            }

            @Override
            public void onFailure(@NotNull Call<JsonResponse> call, @NotNull Throwable t) {
                Log.e("POST Error", t.toString());
                result = t.toString();
            }
        });
    }

    public void deleteMessage(final int position) {

        //Log.d("Deleted:", Long.toString(mAdapter.getItem(position).getID()));
        new RealmController(context).removeItemById(mAdapter.getItem(position).getID());
        mAdapter.notifyDataSetChanged();

        result = null;
        jsonApi.deleteMessage(mAdapter.getItem(position).getFirebase_id())
                .enqueue(new Callback<JsonResponse>() {

            @Override
            public void onResponse(@NotNull Call<JsonResponse> call, @NotNull Response<JsonResponse> response) {
                result = Boolean.toString(true);
            }
            @Override
            public void onFailure(@NotNull Call<JsonResponse> call, @NotNull Throwable t) {
                Log.e("DELETE Error", t.toString());
            }
        });

    }

    public void updateMessage(int position, final String text) {
        result = null;

        new RealmController(context).updateInfo(mAdapter.getItem(position).getID(), text);
        mAdapter.notifyDataSetChanged();

        JsonItemPost jsonItemPost = new JsonItemPost();
        jsonItemPost.setText(text);
        jsonItemPost.setDate(mAdapter.getItem(position).getDate());
        jsonItemPost.setAuthor_id(0L);

        jsonApi.putMessage(mAdapter.getItem(position).getFirebase_id(), jsonItemPost)
                .enqueue(new Callback<JsonItemPost>() {

            @Override
            public void onResponse(@NotNull Call<JsonItemPost> call, @NotNull Response<JsonItemPost> response) {

                if (response.isSuccessful()) {
                    Log.d("POST Success", response.body().toString());
                } else {
                    Log.d("POST Fail", response.body().toString());
                }
                result = Boolean.toString(response.isSuccessful());
            }

            @Override
            public void onFailure(@NotNull Call<JsonItemPost> call, @NotNull Throwable t) {
                Log.e("POST Error", t.toString());
                result = t.toString();
            }
        });

    }

}
