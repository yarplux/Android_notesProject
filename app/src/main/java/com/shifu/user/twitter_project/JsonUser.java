package com.shifu.user.twitter_project;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class JsonUser {
    private String uid;

    JsonUser (String name, String uid) {
        this.uid = name +":"+uid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("uid", uid)
                .toString();
    }
}
