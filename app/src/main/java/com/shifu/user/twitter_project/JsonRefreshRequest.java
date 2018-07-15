package com.shifu.user.twitter_project;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class JsonRefreshRequest {

    @SerializedName("grant_type")
    @Expose
    private String grantType = "refresh_token";

    @SerializedName("refresh_token")
    @Expose
    private String refreshToken;

    JsonRefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("grantType", grantType)
                .append("refreshToken", refreshToken).toString();
    }

}