package com.shifu.user.notes_project.json;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class JsonResponse {
    private String name;

    public JsonResponse (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .toString();
    }
}
