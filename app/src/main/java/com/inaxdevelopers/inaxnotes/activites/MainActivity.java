package com.inaxdevelopers.inaxnotes.activites;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.inaxdevelopers.inaxnotes.AdmobAds;
import com.inaxdevelopers.inaxnotes.MyApplication;
import com.inaxdevelopers.inaxnotes.R;
import com.inaxdevelopers.inaxnotes.ViewModel.NotesViewModel;
import com.inaxdevelopers.inaxnotes.adapter.NotesAdapter;
import com.inaxdevelopers.inaxnotes.model.Notes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private boolean blnStart = false;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final int PERMISSION_REQUEST_CODE = 123;
    NotesAdapter INAxadapter;
    List<Notes> filterNotesAllList;
    TextView hightolow;
    TextView lowtohigh;
    ImageView newNotesBtn;
    TextView nofilter;
    RecyclerView notesRecycler;
    NotesViewModel notesViewModel;
    LinearLayout no_data;
    Toolbar toolbar;
    AdView adView;
    EditText inputSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ID_Find();
        nofilter.setBackgroundResource(R.drawable.filter_selected_shape);
        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);
        ID_CLick();
        AdmobAds.loadBannerAd(this,findViewById(R.id.adContainerView));
    }
    private void ID_Find() {
        newNotesBtn = findViewById(R.id.newNotesBtn);
        notesRecycler = findViewById(R.id.notesRecycler);
        nofilter = findViewById(R.id.no_filter);
        hightolow = findViewById(R.id.high_to_low);
        lowtohigh = findViewById(R.id.low_To_High);
        toolbar = findViewById(R.id.toolbar);
        no_data = findViewById(R.id.no_data);
        inputSearch = findViewById(R.id.inputSearch);
        setSupportActionBar(toolbar);
    }

    private void ID_CLick() {
        nofilter.setOnClickListener(view -> {
            loadData(0);
            hightolow.setBackgroundResource(R.drawable.filter_un_shape);
            lowtohigh.setBackgroundResource(R.drawable.filter_un_shape);
            nofilter.setBackgroundResource(R.drawable.filter_selected_shape);

        });
        hightolow.setOnClickListener(view -> {
            loadData(1);
            hightolow.setBackgroundResource(R.drawable.filter_selected_shape);
            lowtohigh.setBackgroundResource(R.drawable.filter_un_shape);
            nofilter.setBackgroundResource(R.drawable.filter_un_shape);

        });
        lowtohigh.setOnClickListener(view -> {
            loadData(2);
            hightolow.setBackgroundResource(R.drawable.filter_un_shape);
            lowtohigh.setBackgroundResource(R.drawable.filter_selected_shape);
            nofilter.setBackgroundResource(R.drawable.filter_un_shape);

        });
        newNotesBtn.setOnClickListener(view -> {
                    blnStart = true;
                    if (!checkAndRequestPermissions()) {
                        requestPermissionForReadExternalStorage();
                    } else {
                        callnextactivity();
                    }
                }
        );
        notesViewModel.getAllNotes.observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(List<Notes> notes) {
                setAdapter(notes);
                filterNotesAllList = notes;
            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                NotesFilter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private void loadData(int i) {
        if (i == 0) {
            notesViewModel.getAllNotes.observe(this, new Observer<List<Notes>>() {
                @Override
                public void onChanged(List<Notes> notes) {
                    setAdapter(notes);
                    filterNotesAllList = notes;
                }
            });
        } else if (i == 1) {
            notesViewModel.hightolow.observe(this, new Observer<List<Notes>>() {
                @Override
                public void onChanged(List<Notes> notes) {
                    setAdapter(notes);
                    filterNotesAllList = notes;
                }
            });
        } else if (i == 2) {
            notesViewModel.lowtohigh.observe(this, new Observer<List<Notes>>() {
                @Override
                public void onChanged(List<Notes> notes) {
                    setAdapter(notes);
                    filterNotesAllList = notes;
                }
            });
        }
    }

    public void setAdapter(List<Notes> notes) {
        notesRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
        INAxadapter = new NotesAdapter(this, notes);
        notesRecycler.setAdapter(INAxadapter);
        if (notes.isEmpty()) {
            no_data.setVisibility(View.VISIBLE);
        } else {
            no_data.setVisibility(View.GONE);
        }
    }


    public void NotesFilter(String newText) {
        ArrayList<Notes> FilterNames = new ArrayList<>();
        for (Notes notes : filterNotesAllList) {
            if (notes.notesTitle.contains(newText) || notes.notesSubtitle.contains(newText)) {
                FilterNames.add(notes);
            }
        }
        INAxadapter.searchNotes(FilterNames);
    }



    private boolean checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_MEDIA_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
        return checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
    public void requestPermissionForReadExternalStorage() {
        String[] strings;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            strings = new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.ACCESS_MEDIA_LOCATION};
        } else {
            strings = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
        }
        try {
            ActivityCompat.requestPermissions(this, strings, PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                perms.put(WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && perms.get(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        if (blnStart == true) {
                            callnextactivity();
                        }
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                            showDialogOK("Permission required for this app", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            checkAndRequestPermissions();
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            break;
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            }

            case PERMISSION_REQUEST_CODE:
                callnextactivity();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            callnextactivity();
        } else {
            Toast.makeText(this, "Please allow permission....", Toast.LENGTH_SHORT).show();
        }
    }

    private void callnextactivity() {
        startActivity(new Intent(this, InsertNotesActivity.class));
    }


    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
}

