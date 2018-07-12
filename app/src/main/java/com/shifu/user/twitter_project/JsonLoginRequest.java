package com.shifu.user.twitter_project;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class JsonLoginRequest {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("returnSecureToken")
    @Expose
    private boolean returnSecureToken = true;

    JsonLoginRequest(String email, String password) {
        this.password = password;
        this.email = email;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("email", email)
                .append("password", password)
                .toString();
    }

}