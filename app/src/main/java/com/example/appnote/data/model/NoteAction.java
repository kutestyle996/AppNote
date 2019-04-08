package com.example.appnote.data.model;

import android.support.annotation.StringDef;

import static com.example.appnote.data.model.NoteAction.BACK;
import static com.example.appnote.data.model.NoteAction.DELETE;
import static com.example.appnote.data.model.NoteAction.EDIT_NOTE;
import static com.example.appnote.data.model.NoteAction.NEW_NOTE;
import static com.example.appnote.data.model.NoteAction.NEXT;
import static com.example.appnote.data.model.NoteAction.PREVIOUS;
import static com.example.appnote.data.model.NoteAction.SHARE;

@StringDef({NEW_NOTE, EDIT_NOTE, BACK, PREVIOUS, DELETE, SHARE, NEXT})
public @interface NoteAction {
    String NEW_NOTE = "NEW_NOTE";
    String EDIT_NOTE = "EDIT_NOTE";
    String BACK = "BACK";
    String PREVIOUS = "PREVIOUS";
    String DELETE = "DELETE";
    String SHARE = "SHARE";
    String NEXT = "NEXT";
}
