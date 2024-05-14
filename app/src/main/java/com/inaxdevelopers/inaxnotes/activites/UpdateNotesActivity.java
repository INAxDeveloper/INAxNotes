package com.inaxdevelopers.inaxnotes.activites;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.inaxdevelopers.inaxnotes.R;
import com.inaxdevelopers.inaxnotes.ViewModel.NotesViewModel;
import com.inaxdevelopers.inaxnotes.databinding.ActivityUpdateNotesBinding;
import com.inaxdevelopers.inaxnotes.model.Notes;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;


public class UpdateNotesActivity extends AppCompatActivity {
    ActivityUpdateNotesBinding binding;
    int iid;
    NotesViewModel notesViewModel;
    String priority = "1";
    String snotes;
    String spriority;
    String ssubtitle;
    String stitle;
    String imagepath;
    String selectedImagePath = "";
    private static final int CAMERA_REQUEST = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUpdateNotesBinding inflate = ActivityUpdateNotesBinding.inflate(getLayoutInflater());
        binding = inflate;
        setContentView(inflate.getRoot());
        iid = getIntent().getIntExtra("id", 0);
        stitle = getIntent().getStringExtra("title");
        ssubtitle = getIntent().getStringExtra("subtitle");
        spriority = getIntent().getStringExtra("priority");
        snotes = getIntent().getStringExtra("note");
        imagepath = getIntent().getStringExtra("image");
        binding.upTitle.setText(stitle);
        binding.upSubtitle.setText(ssubtitle);
        binding.upNotes.setText(snotes);
        SetImageAndURL();
        if (spriority.equals("1")) {
            binding.greenPriority.setImageResource(R.drawable.ic_done);
        } else if (spriority.equals("2")) {
            binding.yellowPriority.setImageResource(R.drawable.ic_done);
        } else if (spriority.equals("3")) {
            binding.redPriority.setImageResource(R.drawable.ic_done);
        }
        setSupportActionBar(binding.toolbarUpdate);
        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);
        IDClick();
    }

    private void SetImageAndURL() {
        if (!imagepath.isEmpty() || imagepath.equals(" ")) {
            RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeFile(imagepath));
            roundedDrawable.setCornerRadius(20f);
            binding.updeleteImage.setVisibility(View.VISIBLE);
            binding.upshowImages.setVisibility(View.VISIBLE);
            binding.frameImage.setVisibility(View.VISIBLE);
            binding.upshowImages.setImageDrawable(roundedDrawable);
            if (selectedImagePath.isEmpty() && selectedImagePath.equals("")) {
                selectedImagePath = imagepath;
            }
        } else {
            binding.updeleteImage.setVisibility(View.GONE);
            binding.upshowImages.setVisibility(View.GONE);
            binding.frameImage.setVisibility(View.GONE);
        }
    }

    private void IDClick() {
        binding.addImage.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Image");
            startActivityForResult(chooserIntent, CAMERA_REQUEST);
        });
        binding.updeleteImage.setOnClickListener(v -> {
            binding.upshowImages.setImageBitmap(null);
            selectedImagePath = "";
            binding.updeleteImage.setVisibility(View.GONE);
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
        binding.deleteNotes.setOnClickListener(v -> {
            BottomSheetDialog sheetDialog = new BottomSheetDialog(this, R.style.BottomSheetStyle);
            View view = LayoutInflater.from(this).inflate(R.layout.delete_bottom_sheet, findViewById(R.id.bottomSheet));
            sheetDialog.setContentView(view);
            TextView yes = view.findViewById(R.id.delete_yes);
            TextView no = view.findViewById(R.id.delete_no);
            yes.setOnClickListener(view2 -> {
                notesViewModel.deleteNote(iid);
                finish();
            });
            no.setOnClickListener(view2 -> sheetDialog.dismiss());
            sheetDialog.show();
        });
        binding.updateNotesBtn.setOnClickListener(view -> {
            String title = binding.upTitle.getText().toString();
            String subtitle = binding.upSubtitle.getText().toString();
            String notes = binding.upNotes.getText().toString();
            String imagePath = selectedImagePath;
            UpdateNotes(title, subtitle, notes, imagePath);
        });
    }


    private void UpdateNotes(String title, String subtitle, String notes, String imagePath) {
        Date date = new Date();
        CharSequence sequence = DateFormat.format("MMMM d, yyyy", date.getTime());
        Notes updateNotes = new Notes();
        updateNotes.id = iid;
        updateNotes.setNotesPriority(priority);
        updateNotes.setNotesDate(sequence.toString());
        updateNotes.setNotesTitle(title);
        updateNotes.setNotesSubtitle(subtitle);
        updateNotes.setNotes(notes);
        updateNotes.setNotesImagePath(imagePath);
        notesViewModel.updateNote(updateNotes);
        Toast.makeText(this, "Notes Updated Successfully", Toast.LENGTH_SHORT).show();
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
                                    binding.upshowImages.setImageDrawable(roundedDrawable);
                                    binding.upshowImages.setVisibility(View.VISIBLE);
                                    binding.updeleteImage.setVisibility(View.VISIBLE);
                                    binding.frameImage.setVisibility(View.VISIBLE);
                                    selectedImagePath = getPathFromUri(selectedImageUri);
                                }
                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }
                            });
                } else {
                    Log.e("Image Uri", "Image Uri is null");
                }

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
