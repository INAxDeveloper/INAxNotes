package com.inaxdevelopers.inaxnotes.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.animation.ObjectAnimator;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inaxdevelopers.inaxnotes.MyApplication;
import com.inaxdevelopers.inaxnotes.R;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class SplashScreenActivity extends AppCompatActivity {
    private final AtomicBoolean isMobileAdsInitializeCalled = new AtomicBoolean(false);
    private static final long COUNTER_TIME_MILLISECONDS = 5000;
    private long secondsRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        createTimer(COUNTER_TIME_MILLISECONDS);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.splash), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeMobileAdsSdk();
    }


    private void createTimer(long time) {

        CountDownTimer countDownTimer = new CountDownTimer(time, 10000) {
            @Override
            public void onTick(long millisUntilFinished) {
                secondsRemaining = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1;
            }

            @Override
            public void onFinish() {
                secondsRemaining = 0;
                Application application = getApplication();
                ((MyApplication) application).showAdIfAvailable(SplashScreenActivity.this, new MyApplication.OnShowAdCompleteListener() {
                    @Override
                    public void onShowAdComplete() {
                        startMainActivity();
                    }
                });
            }
        };
        countDownTimer.start();
    }

    private void initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return;
        }
        MobileAds.initialize(this);
        Application application = getApplication();
        ((MyApplication) application).loadAd(this);

    }
    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
    }
}