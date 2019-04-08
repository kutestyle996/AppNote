package com.example.appnote.application;

import android.app.Application;

import com.example.appnote.data.local.RealmManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class NoteApplication extends Application {
    private static String REALM_NAME = "note.realm";
    private static NoteApplication sApplication;

    public static NoteApplication getApplication() {
        return sApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        // TODO initialize realm
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration
                .Builder().name(REALM_NAME)
                .build();
        Realm.setDefaultConfiguration(config);
        RealmManager.getInstance();
    }
}
