package com.shifu.user.twitter_project.json;

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

    public JsonNewPassRequest(String idToken, String password, boolean returnSecureToken){
        super();
        this.idToken = idToken;
        this.password = password;
        this.returnSecureToken = returnSecureToken;
    }
}
