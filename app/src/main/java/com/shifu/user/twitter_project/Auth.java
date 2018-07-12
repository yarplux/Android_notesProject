package com.shifu.user.twitter_project;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;

class Auth extends Object implements Parcelable{

    private String username;
    private String uid;
    private String idToken;

    Auth (String username, String uid, String idToken) {
        super();

        this.username = username;
        this.uid = uid;
        this.idToken = idToken;
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
}
