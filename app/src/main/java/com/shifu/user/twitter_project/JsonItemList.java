package com.shifu.user.twitter_project;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

public class JsonItemList {

    @SerializedName("messages")
    @Expose
    private Map<String, JsonItem> messages = new HashMap<>();

    public Map<String, JsonItem> getMessages() {
        return messages;
    }

    public void addMessage(String key, JsonItem data) {
        messages.put(key, data);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("messages", messages)
                .toString();
    }

}