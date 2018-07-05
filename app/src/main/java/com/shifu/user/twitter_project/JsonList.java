package com.shifu.user.twitter_project;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class JsonList {

    @SerializedName("items")
    @Expose
    private List<JsonItem> items = null;
    @SerializedName("basepath")
    @Expose
    private String basepath;

    public List<JsonItem> getJsonItems() {
        return items;
    }

    public void setJsonItems(List<JsonItem> items) {
        this.items = items;
    }

    public String getBasepath() {
        return basepath;
    }

    public void setBasepath(String basepath) {
        this.basepath = basepath;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("items", items).append("basepath", basepath).toString();
    }

}