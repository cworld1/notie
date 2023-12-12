package com.cworld.notie.adapter;

public class NoteModel {
    private final String title;
    private final String content;

    public NoteModel(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
