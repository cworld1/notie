package com.cworld.notie.adapter;

public class NoteModel {
    private final String title;
    private final String content;
    private final long editTime;

    public NoteModel(String title, String content, long editTime) {
        this.title = title;
        this.content = content;
        this.editTime = editTime;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public long getEditTime() {
        return editTime;
    }
}
