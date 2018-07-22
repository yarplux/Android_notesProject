package com.shifu.user.twitter_project.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class JsonLoginResponse {

    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("idToken")
    @Expose
    private String idToken;
    @SerializedName("refreshToken")
    @Expose
    private String refreshToken;
    @SerializedName("expiresIn")
    @Expose
    private String expiresIn;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("localId")
    @Expose
    private String localId;


    public String getKind() {
        return kind;
    }

    public String getIdToken() {
        return idToken;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
    public String getExpiresIn() {
        return expiresIn;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("expiresIn", expiresIn)
                .append("email", email)
                .append("localId", localId)
                .toString();
    }

    public String getName() {
        return email.split("@")[0];
    }

    public String getLocalId() {
        return localId;
    }
}