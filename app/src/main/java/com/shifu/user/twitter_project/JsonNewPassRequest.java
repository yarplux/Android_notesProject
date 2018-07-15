package com.shifu.user.twitter_project;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonNewPassRequest {
    @SerializedName("idToken")
    @Expose
    private String idToken;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("returnSecureToken")
    @Expose
    private boolean returnSecureToken;

    JsonNewPassRequest(String idToken, String password, boolean returnSecureToken){
        super();
        this.idToken = idToken;
        this.password = password;
        this.returnSecureToken = returnSecureToken;
    }
}
