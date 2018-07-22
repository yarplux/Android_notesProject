package com.shifu.user.twitter_project.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class JsonNewResponseInner {
    @SerializedName("providerId")
    @Expose
    private String providerId;
    @SerializedName("federatedId")
    @Expose
    private String federatedId;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("photoUrl")
    @Expose
    private String photoUrl;

}
