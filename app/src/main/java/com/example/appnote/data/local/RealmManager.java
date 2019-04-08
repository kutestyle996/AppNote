package com.example.appnote.data.local;

import com.example.appnote.data.model.Note;
import java.util.Date;
import java.util.List;
import io.realm.Realm;
import io.realm.Sort;

public class RealmManager {
    private static RealmManager sInstance;
    private Realm mRealm;
    private List<Note> mNotes;

    public static RealmManager getInstance() {
        if (sInstance == null) {
            sInstance = new RealmManager();
        }
        return sInstance;
    }

    private RealmManager() {
        mRealm = Realm.getDefaultInstance();
        mNotes = mRealm.where(Note.class)
                .sort("mCreateDate", Sort.DESCENDING)
                .findAll();
    }

    public void insertNote(Note note) {
        mRealm.beginTransaction();
        Note copyOfNote = mRealm.copyToRealm(note);
        mRealm.commitTransaction();
    }

    public void deleteNote(final int position) {
        mRealm.beginTransaction();
        mRealm.where(Note.class)
                .sort("mCreateDate", Sort.DESCENDING)
                .findAll()
                .deleteFromRealm(position);
        mRealm.commitTransaction();
    }

    public void addImageNote(Note note, String url ) {
        mRealm.beginTransaction();
        note.getUriStrings().add(url);
        mRealm.commitTransaction();
    }

    public void removeImageNote(Note note, int positon ) {
        mRealm.beginTransaction();
        note.getUriStrings().remove(positon);
        mRealm.commitTransaction();
    }

    public void changeBackgroundColor(Note note, int color) {
        mRealm.beginTransaction();
        note.setColor(color);
        mRealm.commitTransaction();
    }


    public void updateNote(Note note, String title, String content, Date createDate, boolean isAlarm) {
        mRealm.beginTransaction();
        note.setTitle(title);
        note.setContent(content);
        note.setCreateDate(createDate);
        note.setAlarm(isAlarm);
        mRealm.commitTransaction();
    }


    public void updateNote(Note note, String title, String content, String day, String hour, Date createDate, boolean isAlarm) {
        mRealm.beginTransaction();
        note.setTitle(title);
        note.setContent(content);
        note.setCreateDate(createDate);
        note.setAlarm(isAlarm);
        note.setDay(day);
        note.setHour(hour);
        mRealm.commitTransaction();
    }

    public List<Note> getNotes() {
        return mNotes;
    }
}
