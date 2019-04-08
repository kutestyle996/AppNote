package com.example.appnote.data.local;

import com.example.appnote.data.model.Note;

import java.util.Date;
import java.util.List;

public interface IDataSource {

    void getAllNote(DataCallback dataCallback);

    void insertNote(Note note);

    void deleteNote(int position);

    void removeImageNote(Note note, int positon);

    void changeBackgroundColor(Note note, int color);

    void addImageNote(Note note, String url);

    void updateNote(Note note, String title, String content, Date createDate, boolean isAlarm);

    void updateNote(Note note, String title, String content, String day, String hour, Date createDate, boolean isAlarm);

    interface DataCallback<T> {
        void onSuccess(List<T> data);

        void onFail(String message);
    }
}
