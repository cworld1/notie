package com.cworld.notie.adapter;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cworld.notie.R;
import com.cworld.notie.util.FormatHelper;

import java.util.ArrayList;
import java.util.List;

public class NoteItemAdapter extends RecyclerView.Adapter<NoteItemAdapter.NoteViewHolder> {
    private final List<NoteModel> noteList;
    private OnItemClickListener listener;
    private boolean isSelectionMode = false;
    private final List<Integer> selectedItems = new ArrayList<>();

    public NoteItemAdapter(List<NoteModel> noteList) {
        this.noteList = noteList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        NoteModel currentNote = noteList.get(position);
        // format data to show
        String formattedTime = FormatHelper.time(currentNote.getEditTime());
        String formattedTitle = FormatHelper.text(currentNote.getTitle());
        String formattedContent = FormatHelper.text(currentNote.getContent());
        holder.titleTextView.setText(formattedTitle);
        holder.contentTextView.setText(formattedContent);
        holder.editTimeTextView.setText(formattedTime);

        // for selection mode
        if (isSelectionMode) {
            holder.checkbox.setVisibility(View.VISIBLE);
            holder.checkbox.setOnCheckedChangeListener(
                    (buttonView, isChecked) -> toggleSelection(holder.getAdapterPosition()));
            holder.itemView.setOnClickListener(v -> holder.checkbox.toggle());
            return;
        }

        // for normal mode
        holder.checkbox.setVisibility(View.GONE);
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
        public CheckBox checkbox;

        public NoteViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            editTimeTextView = itemView.findViewById(R.id.editTimeTextView);
            checkbox = itemView.findViewById(R.id.checkbox_note);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(NoteModel note);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSelectionMode(boolean selectionMode) {
        isSelectionMode = selectionMode;
        notifyDataSetChanged();
    }

    public void clearSelection() {
        selectedItems.clear();
    }

    public void toggleSelection(int position) {
        if (selectedItems.contains(position)) {
            selectedItems.remove(Integer.valueOf(position));
        } else {
            selectedItems.add(position);
        }
    }

    public List<NoteModel> getSelectedItems() {
        List<NoteModel> selectedNotes = new ArrayList<>();
        for (int position : selectedItems) {
            selectedNotes.add(noteList.get(position));
        }
        return selectedNotes;
    }
}
