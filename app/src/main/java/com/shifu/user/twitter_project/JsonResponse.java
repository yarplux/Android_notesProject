package com.shifu.user.twitter_project;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class JsonResponse {
    private String name;

    JsonResponse () {}

    JsonResponse (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .toString();
    }
}
