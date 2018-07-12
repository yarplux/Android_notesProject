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

    @SerializedName("uid")
    @Expose
    private String uid;

    @SerializedName("retwitUid")
    @Expose
    private String retwitUid;

    JsonItem(){}

    JsonItem(String text, Long date, String username) {
        this.text = text;
        this.date = date;
        this.uid = username;
    }

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
    public String getAuthor() {
        return uid;
    }

    public void setAuthor(String author) {
        this.uid = author;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("text", text)
                .append("date", date)
                .toString();
    }

    public String getRetwitUid() {
        return retwitUid;
    }

    public void setRetwitUid(String retwitUid) {
        this.retwitUid = retwitUid;
    }
}