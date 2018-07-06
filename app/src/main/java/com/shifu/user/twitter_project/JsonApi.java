package com.shifu.user.twitter_project;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JsonApi {

    String  BASE_URL = "";

    @GET("{file}")
    Call<List<JsonItem>> loadMessages(@Path("file") String file);

}
