package com.cworld.notie.adapter;

import java.util.Date;

public class NoteModel {
    private final String title;
    private final String content;
    private final Date editTime;

    public NoteModel(String title, String content, Date editTime) {
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

    public Date getEditTime() {
        return editTime;
    }
}
