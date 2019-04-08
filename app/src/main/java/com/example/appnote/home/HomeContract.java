package com.example.appnote.home;

import com.example.appnote.data.model.Note;
import java.util.List;

public class HomeContract {
    interface View {
        void updateData(List<Note> data);
        void emptyNote();
    }

    interface Presenter {
        void getAllNote();
    }
}
