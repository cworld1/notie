package com.cworld.notie.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.cworld.notie.adapter.NoteModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class NoteFileHelper {
    private static String path;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public NoteFileHelper(Context context, String path) {
        NoteFileHelper.context = context;
        NoteFileHelper.path = path;
    }

    public List<NoteModel> getAllNotes() {
        List<NoteModel> notes = new ArrayList<>();

        try {
            File directory = new File(context.getExternalFilesDir(null), path);
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    String noteName = file.getName().replace(".txt", "");
                    String noteContent = getNoteContent(file);
                    Date lastEditedTime = new Date(file.lastModified());
                    NoteModel note = new NoteModel(noteName, noteContent, lastEditedTime);
                    notes.add(note);
                }
            }
        } catch (Exception ex) {
            Log.d("fileAccess", "读取笔记文件失败");
            ex.printStackTrace();
        }

        return notes;
    }

    public static void setNote(NoteModel note) {
        try {
            File directory = new File(context.getExternalFilesDir(null), path);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directory, note.getTitle() + ".txt");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(note.getContent().getBytes());
            fos.close();
        } catch (IOException ex) {
            Log.d("fileAccess", "写文件失败");
            ex.printStackTrace();
        }
    }


    public static void setNote(NoteModel note, String originTitle) {
        try {
            File directory = new File(context.getExternalFilesDir(null), path);
            File file = new File(directory, originTitle + ".txt");
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        setNote(note);
    }

    private String getNoteContent(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            Scanner scanner = new Scanner(fis);
            StringBuilder content = new StringBuilder();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Log.d("fileAccess", line);
                content.append(line).append("\n");
            }
            scanner.close();
            fis.close();
            return content.toString();
        } catch (IOException ex) {
            Log.d("fileAccess", "读文件失败");
            ex.printStackTrace();
        }
        return null;
    }
}