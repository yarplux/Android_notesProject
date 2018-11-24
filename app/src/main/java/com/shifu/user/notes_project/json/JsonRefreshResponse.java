package com.shifu.user.notes_project.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class JsonRefreshResponse {

    @SerializedName("expires_in")
    @Expose
    private String expiresIn;
    @SerializedName("token_type")
    @Expose
    private String tokenType;
    @SerializedName("refresh_token")
    @Expose
    private String refreshToken;
    @SerializedName("id_token")
    @Expose
    private String idToken;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("project_id")
    @Expose
    private String projectId;


    public String getExpiresIn() {
        return expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
    public String getIdToken() {
        return idToken;
    }
    public String getUserId() {
        return userId;
    }
    public String getProjectId() {
        return projectId;
    }

}