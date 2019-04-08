package com.example.appnote.home;

import com.example.appnote.data.local.IDataSource;
import com.example.appnote.data.local.NoteDataSource;
import java.util.List;

public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View mView;
    private NoteDataSource mNoteDataSource;

    public HomePresenter(HomeContract.View view, NoteDataSource noteDataSource) {
        mNoteDataSource = noteDataSource;
        mView = view;
    }

    @Override
    public void getAllNote() {
        mNoteDataSource.getAllNote(new IDataSource.DataCallback() {
            @Override
            public void onSuccess(List data) {
                mView.updateData(data);
            }

            @Override
            public void onFail(String message) {
                mView.emptyNote();
            }
        });
    }
}
