package com.shifu.user.twitter_project;

import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonApi {

    String  MESSAGES = "messages";
    String  USERS = "users";

    @GET(MESSAGES+".json")
    Call<Map<String, JsonItem>> loadMessages(@Query("orderBy") String authorField, @Query("equalTo") String authorName);

    @POST(MESSAGES+".json")
    Call<JsonResponse> pushMessage(@Body JsonItem jsonBody);

    @DELETE(MESSAGES+"/{firebase_id}.json")
    Call<JsonResponse> deleteMessage(@Path("firebase_id") String firebase_id);

    @PUT(MESSAGES+"/{firebase_id}.json")
    Call<JsonItem> putMessage(@Path("firebase_id") String firebase_id, @Body JsonItem jsonBody);

    @GET(USERS+".json")
    Call<Map<String, JsonResponse>> checkAuthor(@Query("orderBy") String authorField, @Query("equalTo") String authorName);

    @POST(USERS+".json")
    Call<JsonResponse> pushUser(@Body JsonResponse jsonBody);

    @GET(USERS+".json")
    Call<Map<String, JsonResponse>> loadUsers();

    @POST("{action}")
    Call<JsonLoginResponse> login(@Path("action") String action, @Header("Content-Type") String header, @Query("key") String API_KEY, @Body JsonLoginRequest request);

    @POST("setAccountInfo")
    Call<JsonUpdateAuthResponse> updateProfile(@Header("Content-Type") String header, @Query("key") String API_KEY, @Body JsonUpdateAuthRequest request);


}
