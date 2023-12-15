package com.cworld.notie.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cworld.notie.R;
import com.cworld.notie.util.FormatHelper;

import java.util.List;

public class NoteItemAdapter extends RecyclerView.Adapter<NoteItemAdapter.NoteViewHolder> {
    private final List<NoteModel> noteList;
    private OnItemClickListener listener;

    public NoteItemAdapter(List<NoteModel> noteList) {
        this.noteList = noteList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        NoteModel currentNote = noteList.get(position);

        String formattedTime = FormatHelper.time(currentNote.getEditTime());
        String formattedTitle = FormatHelper.text(currentNote.getTitle());
        String formattedContent = FormatHelper.text(currentNote.getContent());

        holder.titleTextView.setText(formattedTitle);
        holder.contentTextView.setText(formattedContent);
        holder.editTimeTextView.setText(formattedTime);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentNote);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView contentTextView;
        public TextView editTimeTextView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            editTimeTextView = itemView.findViewById(R.id.editTimeTextView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(NoteModel note);
    }
}
