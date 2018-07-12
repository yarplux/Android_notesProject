package com.shifu.user.twitter_project;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class JsonUpdateAuthRequest {
    @SerializedName("idToken")
    @Expose
    private String idToken;
    @SerializedName("displayName")
    @Expose
    private String displayName;

    @SerializedName("returnSecureToken")
    @Expose
    private boolean returnSecureToken;

    JsonUpdateAuthRequest(String idToken, String displayName, boolean returnSecureToken){
        super();
        this.idToken = idToken;
        this.displayName = displayName;
        this.returnSecureToken = returnSecureToken;
    }
}
