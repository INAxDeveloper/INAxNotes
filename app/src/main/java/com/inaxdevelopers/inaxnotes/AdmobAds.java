package com.inaxdevelopers.inaxnotes;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class AdmobAds {

    public static AdView adView;
    public static boolean isLoadIronSourceAd = true;
    public static void Init(Context context) {
        MobileAds.initialize(context, initializationStatus -> {
        });
    }

    public static void loadBannerAd(Context context, FrameLayout layout) {
        adView = new AdView(context);
        adView.setAdUnitId(context.getResources().getString(R.string.banner_ad_unit_id));
        layout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdSize adSize = getAdSize((Activity) context);
        adView.setAdSize(adSize);
        adView.loadAd(adRequest);
    }

    private static AdSize getAdSize(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }
}
