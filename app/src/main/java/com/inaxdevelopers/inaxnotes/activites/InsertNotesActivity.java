package com.inaxdevelopers.inaxnotes.activites;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.inaxdevelopers.inaxnotes.R;
import com.inaxdevelopers.inaxnotes.ViewModel.NotesViewModel;
import com.inaxdevelopers.inaxnotes.databinding.ActivityInsertNotesBinding;
import com.inaxdevelopers.inaxnotes.model.Notes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class InsertNotesActivity extends AppCompatActivity {
    private static final int REQ_CODE_SPEECH_INPUT = 344;
    ActivityInsertNotesBinding binding;
    String notes;
    NotesViewModel notesViewModel;
    String priority = "1";
    String subtitle;
    String title;
    String selectedImagePath = "";
    private static final int CAMERA_REQUEST = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);
        setSupportActionBar(binding.toolbarInsert);
        binding.textDateTime.setText(new SimpleDateFormat("dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date()));
        IDClick();
    }

    private void IDClick() {

        binding.addImage.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Image");
            startActivityForResult(chooserIntent, CAMERA_REQUEST);
        });
        binding.deleteImage.setOnClickListener(v -> {
            binding.showImages.setImageBitmap(null);
            selectedImagePath = "";
            binding.deleteImage.setVisibility(View.GONE);
            binding.frameImage.setVisibility(View.GONE);
        });

        binding.greenPriority.setOnClickListener(view -> {
            binding.greenPriority.setImageResource(R.drawable.ic_done);
            binding.yellowPriority.setImageResource(0);
            binding.redPriority.setImageResource(0);
            priority = "1";

        });
        binding.yellowPriority.setOnClickListener(view -> {
            binding.greenPriority.setImageResource(0);
            binding.yellowPriority.setImageResource(R.drawable.ic_done);
            binding.redPriority.setImageResource(0);
            priority = "2";
        });
        binding.redPriority.setOnClickListener(view -> {
            binding.greenPriority.setImageResource(0);
            binding.yellowPriority.setImageResource(0);
            binding.redPriority.setImageResource(R.drawable.ic_done);
            priority = "3";
        });
        binding.doneNotesBtn.setOnClickListener(view -> {
            title = binding.notesTitle.getText().toString();
            subtitle = binding.notesSubtitle.getText().toString();
            String obj = binding.notesData.getText().toString();
            notes = obj;
            String path;
            path = selectedImagePath;
            String dataTime = binding.textDateTime.getText().toString();
            CreateNotes(title, dataTime, subtitle, obj, path);
        });
        binding.voice.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak something...");

            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        });
    }

    private void CreateNotes(String title, String date, String subtitle, String notes, String path) {
        Notes instnotes = new Notes();
        instnotes.setNotesPriority(priority);
        instnotes.setNotesTitle(title);
        instnotes.setNotesDate(date);
        instnotes.setNotesSubtitle(subtitle);
        instnotes.setNotes(notes);
        instnotes.setNotesImagePath(path);
        notesViewModel.insertNote(instnotes);
        Toast.makeText(this, R.string.notesCreated, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (CAMERA_REQUEST == requestCode && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    Glide.with(this)
                            .asBitmap()
                            .load(selectedImageUri)
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                                    roundedDrawable.setCornerRadius(20f);
                                    binding.showImages.setImageDrawable(roundedDrawable);
                                    binding.showImages.setVisibility(View.VISIBLE);
                                    binding.deleteImage.setVisibility(View.VISIBLE);
                                    binding.frameImage.setVisibility(View.VISIBLE);
                                    selectedImagePath = getPathFromUri(selectedImageUri);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }
                            });
                } else {
                    Toast.makeText(this, "Image Not Supported", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String speechText = result.get(0);
                binding.notesData.setText(speechText);
            }
        }
    }

    private String getPathFromUri(Uri contentUri) {
        String filePath;
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            filePath = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }
}
