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

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("retwitted")
    @Expose
    private String retwitted;

    JsonItem(){}

    JsonItem(String text, Long date, String username) {
        this.text = text;
        this.date = date;
        this.author = username;
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

    public String getRetwitted() {
        return retwitted;
    }

    public void setRetwitted(String retwitted) {
        this.retwitted = retwitted;
    }
}