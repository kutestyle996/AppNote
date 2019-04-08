package com.example.appnote.detail;

import android.support.annotation.ColorInt;
import com.example.appnote.data.model.Note;
import com.example.appnote.data.model.NoteAction;
import java.util.Date;
import java.util.List;

public class DetailContract {
    interface View {
        void setupUi(@NoteAction String action);

        void updateBackgroundColor(@ColorInt int color);

        void showDialogColorPicker();

        void showDialogDatePicker();

        void showDialogTimePicker();

        void previewImage(String url);

        void setupForNewNote();

        void setupForEditNote();

        void doneAndUpdate();

        void setupArlarm();

        void selectImage();

        void setNotes(List<Note> notes);

        void share();

        void showDialogConfirm(@NoteAction final String action);

        void scheduleNotification(boolean isCancel);
    }

    interface Presenter {
        void setupUi(@NoteAction String action);

        void changeBackgroundColor(Note note, @ColorInt int color);

        void saveNewNote(Note note);

        void getAllNote();

        void deleteNote(int position);

        void addImageNote(Note note, String url);

        void removeImageNote(Note note, int positon);

        void updateNote(Note note, String title, String content, Date createDate, boolean isAlarm);

        void updateNote(Note note, String title, String content, String day, String hour, Date createDate, boolean isAlarm);

        void share();

        void showDialogConfirm(@NoteAction final String action);

        void scheduleNotification(boolean isCancel);
    }
}
