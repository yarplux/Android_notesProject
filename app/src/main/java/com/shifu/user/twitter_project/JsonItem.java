package com.shifu.user.twitter_project;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class JsonItem {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("date")
    @Expose
    private Long date;

    public String getText() {
        return text;
    }

    public void setText(String data) {
        this.text = data;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long data) {
        this.date = data;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("text", text)
                .append("date", date)
                .toString();
    }

}