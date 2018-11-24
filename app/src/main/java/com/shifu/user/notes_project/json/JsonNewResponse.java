package com.shifu.user.notes_project.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class JsonNewResponse {

    @SerializedName("kind")
    @Expose
    private String kind;

    @SerializedName("localId")
    @Expose
    private String localId;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("passwordHash")
    @Expose
    private String passwordHash;

    @SerializedName("providerUserInfo")
    @Expose
    private List<JsonNewResponseInner> providerUserInfo = null;

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

    public String getPasswordHash() {
        return passwordHash;
    }

    public List<JsonNewResponseInner> getProviderUserInfo() {
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
                .append("passwordHash", passwordHash)
                .append("providerUserInfo", providerUserInfo)
                .append("idToken", idToken)
                .append("refreshToken", refreshToken)
                .append("expiresIn", expiresIn).toString();
    }

}