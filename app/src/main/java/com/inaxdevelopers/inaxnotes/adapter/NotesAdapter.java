package com.inaxdevelopers.inaxnotes.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.inaxdevelopers.inaxnotes.R;
import com.inaxdevelopers.inaxnotes.activites.MainActivity;
import com.inaxdevelopers.inaxnotes.activites.UpdateNotesActivity;
import com.inaxdevelopers.inaxnotes.model.Notes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.notesViewholder> {
    List<Notes> allNotesitem;
    MainActivity mainActivity;
    List<Notes> notes;

    public NotesAdapter(MainActivity mainActivity, List<Notes> notes) {
        this.mainActivity = mainActivity;
        this.notes = notes;
        this.allNotesitem = new ArrayList(notes);
    }

    public void searchNotes(List<Notes> filterredName) {
        this.notes = filterredName;
        notifyDataSetChanged();
    }

    @Override
    public notesViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new notesViewholder(LayoutInflater.from(mainActivity).inflate(R.layout.item_notes, parent, false));
    }

    @Override
    public void onBindViewHolder(notesViewholder holder, int position) {

        final Notes note = notes.get(position);
        String str = notes.get(position).getNotesPriority();
        int i = str.hashCode();
        if (i == 49) {
            if (str.equals("1")) {
                holder.notesPriority.setBackgroundResource(R.drawable.green_shape);
            }
        } else if (i == 50) {
            if (str.equals("2")) {
                holder.notesPriority.setBackgroundResource(R.drawable.yellow_shape);
            }
        } else if (i == 51) {
            if (str.equals("3")) {
                holder.notesPriority.setBackgroundResource(R.drawable.red_shape);
            }
        }

        if (!notes.get(position).getNotesTitle().isEmpty()) {
            holder.title.setText(notes.get(position).getNotesTitle());
        } else {
            holder.title.setVisibility(View.GONE);
        }
        if (!notes.get(position).getNotesSubtitle().isEmpty()) {
            holder.subtitle.setText(notes.get(position).getNotesSubtitle());
        } else {
            holder.subtitle.setVisibility(View.GONE);
        }
        if (!notes.get(position).getNotesDate().isEmpty()) {
            holder.notesDate.setText(notes.get(position).getNotesDate());
        } else {
            holder.notesDate.setVisibility(View.GONE);
        }
        if (notes.get(position).getNotesImagePath() != null) {
//            Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(note.getNotesImagePath()), 90, 130);
            Glide.with(mainActivity)
                    .asBitmap()
                    .load(note.getNotesImagePath())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(mainActivity.getResources(), resource);
                            roundedDrawable.setCornerRadius(20f);
                            holder.show_images.setImageDrawable(roundedDrawable);
                            Log.e("abc", "onResourceReady: " + roundedDrawable);
                            holder.show_images.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });

            holder.show_images.setVisibility(View.VISIBLE);
//            holder.show_images.setImageBitmap(thumbImage);

        } else {
            holder.show_images.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, UpdateNotesActivity.class);
                intent.putExtra("id", note.id);
                intent.putExtra("title", note.getNotesTitle());
                intent.putExtra("subtitle", note.getNotesSubtitle());
                intent.putExtra("priority", note.getNotesPriority());
                intent.putExtra("image", note.getNotesImagePath());
                intent.putExtra("note", note.getNotes());
                mainActivity.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return this.notes.size();
    }


    public static class notesViewholder extends RecyclerView.ViewHolder {
        TextView notesDate;
        View notesPriority;
        TextView subtitle;
        TextView title;
        ImageView show_images;

        public notesViewholder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notesTitle);
            subtitle = itemView.findViewById(R.id.notesSubtitle);
            notesDate = itemView.findViewById(R.id.notesDate);
            notesPriority = itemView.findViewById(R.id.notesPriority);
            show_images = itemView.findViewById(R.id.show_images_adapter);
        }
    }
}
