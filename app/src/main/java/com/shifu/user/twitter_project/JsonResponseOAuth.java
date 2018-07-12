package com.shifu.user.twitter_project;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class JsonResponseOAuth {

    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("federatedId")
    @Expose
    private String federatedId;
    @SerializedName("providerId")
    @Expose
    private String providerId;
    @SerializedName("localId")
    @Expose
    private String localId;
    @SerializedName("emailVerified")
    @Expose
    private boolean emailVerified;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("oauthIdToken")
    @Expose
    private String oauthIdToken;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("idToken")
    @Expose
    private String idToken;
    @SerializedName("photoUrl")
    @Expose
    private String photoUrl;
    @SerializedName("refreshToken")
    @Expose
    private String refreshToken;
    @SerializedName("expiresIn")
    @Expose
    private String expiresIn;
    @SerializedName("rawUserInfo")
    @Expose
    private String rawUserInfo;

    public JsonResponseOAuth() {
    }

    public JsonResponseOAuth(String kind, String federatedId, String providerId, String localId, boolean emailVerified, String email, String oauthIdToken, String firstName, String lastName, String fullName, String displayName, String idToken, String photoUrl, String refreshToken, String expiresIn, String rawUserInfo) {
        super();
        this.kind = kind;
        this.federatedId = federatedId;
        this.providerId = providerId;
        this.localId = localId;
        this.emailVerified = emailVerified;
        this.email = email;
        this.oauthIdToken = oauthIdToken;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.displayName = displayName;
        this.idToken = idToken;
        this.photoUrl = photoUrl;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.rawUserInfo = rawUserInfo;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getFederatedId() {
        return federatedId;
    }

    public void setFederatedId(String federatedId) {
        this.federatedId = federatedId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOauthIdToken() {
        return oauthIdToken;
    }

    public void setOauthIdToken(String oauthIdToken) {
        this.oauthIdToken = oauthIdToken;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRawUserInfo() {
        return rawUserInfo;
    }

    public void setRawUserInfo(String rawUserInfo) {
        this.rawUserInfo = rawUserInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("kind", kind)
                .append("federatedId", federatedId)
                .append("providerId", providerId)
                .append("localId", localId)
                .append("emailVerified", emailVerified)
                .append("email", email)
                .append("oauthIdToken", oauthIdToken)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("fullName", fullName)
                .append("displayName", displayName)
                .append("idToken", idToken)
                .append("photoUrl", photoUrl)
                .append("refreshToken", refreshToken)
                .append("expiresIn", expiresIn)
                .append("rawUserInfo", rawUserInfo)
                .toString();
    }

}