package com.example.appnote.detail;

import android.support.annotation.ColorInt;
import com.example.appnote.data.local.IDataSource;
import com.example.appnote.data.local.NoteDataSource;
import com.example.appnote.data.model.Note;
import com.example.appnote.data.model.NoteAction;
import java.util.Date;
import java.util.List;

public class DetailPresenter implements DetailContract.Presenter {
    private DetailContract.View mView;
    private NoteDataSource mNoteDataSource;

    public DetailPresenter(DetailContract.View view, NoteDataSource noteDataSource) {
        mView = view;
        mNoteDataSource = noteDataSource;
    }

    @Override
    public void setupUi(@NoteAction String action) {
        mView.setupUi(action);
    }

    @Override
    public void changeBackgroundColor(Note note, @ColorInt int color) {
        mNoteDataSource.changeBackgroundColor(note, color);
        mView.updateBackgroundColor(color);
    }

    @Override
    public void saveNewNote(Note note) {
        mNoteDataSource.insertNote(note);
        mView.doneAndUpdate();
    }

    @Override
    public void updateNote(Note note, String title, String content, Date createDate, boolean isAlarm) {
        mNoteDataSource.updateNote(note, title, content, createDate, isAlarm);
        mView.doneAndUpdate();
    }

    @Override
    public void updateNote(Note note, String title, String content, String day, String hour, Date createDate, boolean isAlarm) {
        mNoteDataSource.updateNote(note, title, content, day, hour, createDate, isAlarm);
        mView.doneAndUpdate();
    }

    @Override
    public void share() {
        mView.share();
    }

    @Override
    public void showDialogConfirm(String action) {
        mView.showDialogConfirm(action);
    }

    @Override
    public void scheduleNotification(boolean isCancel) {
        mView.scheduleNotification(isCancel);
    }

    @Override
    public void getAllNote() {
        mNoteDataSource.getAllNote(new IDataSource.DataCallback() {
            @Override
            public void onSuccess(List data) {
                mView.setNotes(data);
            }

            @Override
            public void onFail(String message) {}
        });
    }

    @Override
    public void addImageNote(Note note, String url) {
        mNoteDataSource.addImageNote(note, url);
    }

    @Override
    public void removeImageNote(Note note, int positon) {
        mNoteDataSource.removeImageNote(note, positon);
    }

    @Override
    public void deleteNote(int position) {
        mNoteDataSource.deleteNote(position);
        mView.doneAndUpdate();
    }
}