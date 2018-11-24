package com.shifu.user.notes_project.json;

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

    @SerializedName("uid")
    @Expose
    private String uid;

    public JsonMsg(String text, Long date, String uid) {
        this.text = text;
        this.date = date;
        this.uid = uid;
    }

    public String getText() {
        return text;
    }

    public Long getDate() {
        return date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("text", text)
                .append("date", date)
                .toString();
    }
}