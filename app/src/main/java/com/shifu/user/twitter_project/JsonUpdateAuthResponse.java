package com.shifu.user.twitter_project;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class JsonUpdateAuthResponse {

    @SerializedName("kind")
    @Expose
    private String kind;

    @SerializedName("localId")
    @Expose
    private String localId;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("displayName")
    @Expose
    private String displayName;

    @SerializedName("photoUrl")
    @Expose
    private String photoUrl;

    @SerializedName("passwordHash")
    @Expose
    private String passwordHash;

    @SerializedName("providerUserInfo")
    @Expose
    private List<JsonUpdateAuthResponseInner> providerUserInfo = null;

    @SerializedName("idToken")
    @Expose
    private String idToken;

    @SerializedName("refreshToken")
    @Expose
    private String refreshToken;

    @SerializedName("expiresIn")
    @Expose
    private String expiresIn;

    public String getKind() {
        return kind;
    }

    public String getLocalId() {
        return localId;
    }
    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public List<JsonUpdateAuthResponseInner> getProviderUserInfo() {
        return providerUserInfo;
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
                .append("kind", kind)
                .append("localId", localId)
                .append("email", email)
                .append("displayName", displayName)
                .append("photoUrl", photoUrl)
                .append("passwordHash", passwordHash)
                .append("providerUserInfo", providerUserInfo)
                .append("idToken", idToken)
                .append("refreshToken", refreshToken)
                .append("expiresIn", expiresIn).toString();
    }

}