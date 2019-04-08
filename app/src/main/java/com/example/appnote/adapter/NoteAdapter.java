package com.example.appnote.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.appnote.R;
import com.example.appnote.data.model.Note;
import com.example.appnote.utils.Utility;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    public LayoutInflater layoutInflater;
    private List<Note> mNotes;
    private NoteClickListener mNoteClickListener;

    public NoteAdapter(Context context, NoteClickListener noteClickListener) {
        layoutInflater = LayoutInflater.from(context);
        mNoteClickListener = noteClickListener;
    }

    public void setNotes(List<Note> notes) {
        mNotes = notes;
    }

    @Override
    @NonNull
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                             int viewType) {
        View view = layoutInflater.inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.binData(mNotes.get(position));
        holder.setNoteClickListener(mNoteClickListener);
    }

    @Override
    public int getItemCount() {
        return mNotes != null ? mNotes.size() : 0;
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private TextView mTitle;
        private TextView mContent;
        private TextView mCreateDate;
        private ImageView mAlarm;
        private CardView mViewParent;
        private NoteClickListener mNoteClickListener;


        public NoteViewHolder(View view) {
            super(view);
            mViewParent = view.findViewById(R.id.viewParent);
            mTitle = view.findViewById(R.id.textTitle);
            mContent = view.findViewById(R.id.textContent);
            mAlarm = view.findViewById(R.id.imageAlarm);
            mCreateDate = view.findViewById(R.id.tvCreateDate);

            mViewParent.setOnClickListener(this);
        }

        private void binData(Note note) {
            mTitle.setText(note.getTitle());
            mContent.setText(note.getContent());
            mCreateDate.setText(Utility.partDateToString(note.getCreateDate()));
            if (!note.isAlarm()) {
                mAlarm.setVisibility(View.GONE);
            }
            mViewParent.setBackgroundColor(note.getColor());
        }

        private void setNoteClickListener(NoteClickListener noteClickListener) {
            mNoteClickListener = noteClickListener;
        }

        @Override
        public void onClick(View v) {
            mNoteClickListener.onClickItem(getAdapterPosition());
        }
    }

    public interface NoteClickListener {
        void onClickItem(int position);
    }
}
