package com.shifu.user.twitter_project;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.builder.ToStringBuilder;

class Auth implements Parcelable{

    private String username;
    private String uid;
    private String idToken;
    private String refresh;

    Auth (String username, String uid, String idToken, String refresh) {
        this.username = username;
        this.uid = uid;
        this.idToken = idToken;
        this.refresh = refresh;
    }

    Auth (MessagesAuthor auth) {
        this.username = auth.getUsername();
        this.uid = auth.getUid();
        this.idToken = auth.getIdToken();
        this.refresh = auth.getRefreshToken();
    }

    public String getUsername() {
        return username;
    }

    public String getUid() {
        return uid;
    }

    public String getIdToken() {
        return idToken;
    }

    @Override
    public String toString(){
        return new ToStringBuilder(this)
                .append("uname", username)
                .append("uid", uid)
                .append("idToken", idToken)
                .append("refreshToken", refresh)
                .toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{username, uid, idToken});
    }

    public static final Parcelable.Creator<Auth> CREATOR = (new Parcelable.Creator<Auth>() {
        public Auth createFromParcel(Parcel in) {
            return new Auth(in);
        }

        @Override
        public Auth[] newArray(int i) {
            return new Auth[0];
        }
    });

    private Auth(Parcel parcel){
        String[] tmp = new String[3];
        parcel.readStringArray(tmp);
        username = tmp[0];
        uid = tmp[1];
        idToken = tmp[2];
    }

    public String getRefresh() {
        return refresh;
    }

}
