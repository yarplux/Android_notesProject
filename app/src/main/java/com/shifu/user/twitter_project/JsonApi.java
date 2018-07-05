package com.shifu.user.twitter_project;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JsonApi {

    String  BASE_URL = "api/v1/Articles/";

    @GET(BASE_URL+"{type}")
    Call<JsonList> loadTitles(@Path("type") String type);

}
