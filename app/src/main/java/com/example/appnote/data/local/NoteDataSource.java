package com.example.appnote.data.local;

import com.example.appnote.data.model.Note;
import java.util.Date;
import java.util.List;

public class NoteDataSource implements IDataSource {
    private static NoteDataSource sInstance;

    public static NoteDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new NoteDataSource();
        }
        return sInstance;
    }

    @Override
    public void getAllNote(DataCallback dataCallback) {
        List<Note> notes = RealmManager.getInstance().getNotes();
        if (notes != null) {
            dataCallback.onSuccess(notes);
        } else dataCallback.onFail(null);
    }

    @Override
    public void insertNote(Note note) {
        RealmManager.getInstance().insertNote(note);
    }

    @Override
    public void updateNote(Note note, String title, String content, Date createDate, boolean isAlarm) {
        RealmManager.getInstance().updateNote(note, title, content, createDate, isAlarm);
    }

    @Override
    public void updateNote(Note note, String title, String content, String day, String hour, Date createDate, boolean isAlarm) {
        RealmManager.getInstance().updateNote(note, title, content, day, hour, createDate, isAlarm);
    }

    @Override
    public void deleteNote(int position) {
        RealmManager.getInstance().deleteNote(position);
    }

    @Override
    public void removeImageNote(Note note, int positon) {
        RealmManager.getInstance().removeImageNote(note, positon);
    }

    @Override
    public void changeBackgroundColor(Note note, int color) {
        RealmManager.getInstance().changeBackgroundColor(note, color);
    }

    @Override
    public void addImageNote(Note note, String url) {
        RealmManager.getInstance().addImageNote(note, url);
    }
}
