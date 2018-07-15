package com.shifu.user.twitter_project;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class JsonMsg {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("date")
    @Expose
    private Long date;

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("retwittted")
    @Expose
    private String retwitted;

    JsonMsg(String text, Long date, String uid) {
        this.text = text;
        this.date = date;
        this.author = uid;
        this.retwitted = "";
    }

    public String getText() {
        return text;
    }

    public Long getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("text", text)
                .append("date", date)
                .toString();
    }

    public String getRetwitUid() {
        return retwitted;
    }

    public void setRetwitUid(String retwitUid) {
        this.retwitted = retwitUid;
    }
}