package com.example.appnote.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.example.appnote.R;
import com.example.appnote.adapter.NoteAdapter;
import com.example.appnote.data.local.NoteDataSource;
import com.example.appnote.data.model.Constants;
import com.example.appnote.data.model.Note;
import com.example.appnote.data.model.NoteAction;
import com.example.appnote.detail.DetailActivity;
import com.example.appnote.utils.Utility;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeContract.View,
        View.OnClickListener, NoteAdapter.NoteClickListener {
    public static final float WIDTH_NOTE = 200;
    private HomePresenter mHomePresenter;
    private RecyclerView mRecyclerNote;
    private ImageView mButtonNewNote;
    private FrameLayout viewEmptyNote;

    private NoteAdapter mNoteAdapter;
    private List<Note> mNotes = new ArrayList<>();

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        return intent;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mHomePresenter.getAllNote();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mHomePresenter = new HomePresenter(this, NoteDataSource.getInstance());
        initViews();
        mHomePresenter.getAllNote();
    }

    @Override
    public void updateData(List<Note> data) {
        mNotes = data;
        mNoteAdapter.setNotes(mNotes);
        mRecyclerNote.setAdapter(mNoteAdapter);
        viewEmptyNote.setVisibility(View.GONE);
        if (data.size() == 0) emptyNote();
    }

    @Override
    public void emptyNote() {
        viewEmptyNote.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_new_note:
                Intent intent = DetailActivity.getIntent(this);
                intent.putExtra(Constants.Extra.EXTRA_ACTION, NoteAction.NEW_NOTE);
                startActivity(intent);
                break;

        }
    }

    private void initViews() {
        mRecyclerNote = findViewById(R.id.recyclerNote);
        mButtonNewNote = findViewById(R.id.button_new_note);
        viewEmptyNote = findViewById(R.id.viewEmptyNote);
        mButtonNewNote.setOnClickListener(this);

        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(this,
                        Utility.calculateNoOfColumns(this, WIDTH_NOTE));
        mRecyclerNote.setLayoutManager(gridLayoutManager);
        mNotes.clear();
        mNoteAdapter = new NoteAdapter(this, this);
    }

    @Override
    public void onClickItem(int position) {
        Intent intent = DetailActivity.getIntent(this);
        intent.putExtra(Constants.Extra.EXTRA_POSITION, position);
        intent.putExtra(Constants.Extra.EXTRA_ACTION, NoteAction.EDIT_NOTE);
        startActivity(intent);
    }
}
