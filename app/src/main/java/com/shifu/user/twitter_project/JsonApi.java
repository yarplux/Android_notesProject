package com.shifu.user.twitter_project;

import com.google.gson.JsonElement;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface JsonApi {

    String  BASE_URL = "messages";

    @GET("{file}")
    Call<Map<String, JsonItem>> loadMessages(@Path("file") String file);

    @POST(BASE_URL+".json")
    Call<JsonResponse> pushMessage(@Body JsonItemPost jsonBody);

    @DELETE(BASE_URL+"/{firebase_id}.json")
    Call<JsonResponse> deleteMessage(@Path("firebase_id") String firebase_id);

    @PUT(BASE_URL+"/{firebase_id}.json")
    Call<JsonItemPost> putMessage(@Path("firebase_id") String firebase_id, @Body JsonItemPost jsonBody);

}
