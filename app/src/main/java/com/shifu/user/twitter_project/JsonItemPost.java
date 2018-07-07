package com.shifu.user.twitter_project;

class JsonItemPost {
    private String text;
    private Long date;
    private Long author_id;

    JsonItemPost(String text, Long date, Long author_id) {
        this.text = text;
        this.date = date;
        this.author_id = author_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(Long author_id) {
        this.author_id = author_id;
    }
}
