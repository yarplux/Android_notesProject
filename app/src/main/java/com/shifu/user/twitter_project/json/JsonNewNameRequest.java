package com.shifu.user.twitter_project.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonNewNameRequest {
    @SerializedName("idToken")
    @Expose
    private String idToken;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("returnSecureToken")
    @Expose
    private boolean returnSecureToken;

    public JsonNewNameRequest(String idToken, String email, boolean returnSecureToken){
        super();
        this.idToken = idToken;
        this.email = email;
        this.returnSecureToken = returnSecureToken;
    }
}
