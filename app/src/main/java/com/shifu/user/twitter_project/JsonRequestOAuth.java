package com.shifu.user.twitter_project;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class JsonRequestOAuth {

    @SerializedName("requestUri")
    @Expose
    private String requestUri;
    @SerializedName("postBody")
    @Expose
    private String postBody;
    @SerializedName("returnSecureToken")
    @Expose
    private boolean returnSecureToken = true;
    @SerializedName("returnIdpCredential")
    @Expose
    private boolean returnIdpCredential = true;

    public JsonRequestOAuth() {
    }

    public JsonRequestOAuth(String requestUri, String postBody, boolean returnSecureToken, boolean returnIdpCredential) {
        super();
        this.requestUri = requestUri;
        this.postBody = postBody;
        this.returnSecureToken = returnSecureToken;
        this.returnIdpCredential = returnIdpCredential;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public boolean isReturnSecureToken() {
        return returnSecureToken;
    }

    public void setReturnSecureToken(boolean returnSecureToken) {
        this.returnSecureToken = returnSecureToken;
    }

    public boolean isReturnIdpCredential() {
        return returnIdpCredential;
    }

    public void setReturnIdpCredential(boolean returnIdpCredential) {
        this.returnIdpCredential = returnIdpCredential;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("requestUri", requestUri)
                .append("postBody", postBody)
                .append("returnSecureToken", returnSecureToken)
                .append("returnIdpCredential", returnIdpCredential)
                .toString();
    }

}