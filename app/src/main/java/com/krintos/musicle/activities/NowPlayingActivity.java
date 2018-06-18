package com.krintos.musicle.activities;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.afollestad.appthemeengine.Config;
import com.afollestad.appthemeengine.customizers.ATEActivityThemeCustomizer;
import com.afollestad.appthemeengine.customizers.ATEToolbarCustomizer;
import com.krintos.musicle.R;
import com.krintos.musicle.utils.Constants;
import com.krintos.musicle.utils.Helpers;
import com.krintos.musicle.utils.NavigationUtils;
import com.krintos.musicle.utils.PreferencesUtility;

public class NowPlayingActivity extends BaseActivity implements ATEActivityThemeCustomizer, ATEToolbarCustomizer {
    String ateKey;
    int accentColor;
    public View anim_view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nowplaying);
        ateKey = Helpers.getATEKey(this);
        accentColor = Config.accentColor(this, ateKey);

        anim_view =(View) findViewById(R.id.anim_view);
        GradientDrawable gd = (GradientDrawable) anim_view.getBackground();
        int width_px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        gd.setStroke(width_px, accentColor);
        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(1000);
        anim.setInterpolator(new LinearInterpolator());

        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setRepeatMode(Animation.REVERSE);

        SharedPreferences warningversionvk = getSharedPreferences("PREFS", 0);
        String  edge_states = warningversionvk.getString("edge_state_on_off", "");
        FrameLayout edge_layout=(FrameLayout) findViewById(R.id.container);
        if (!edge_states.equals("edge_off")){
            int padding_in_dp = 3;  // 6 dps
            final float scale = getResources().getDisplayMetrics().density;
            int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
            assert edge_layout != null;
            edge_layout.setPadding(padding_in_px,padding_in_px,padding_in_px,padding_in_px);
            anim_view.setVisibility(View.VISIBLE);
            anim_view.startAnimation(anim);
        }


        SharedPreferences prefs = getSharedPreferences(Constants.FRAGMENT_ID, Context.MODE_PRIVATE);
        String fragmentID = prefs.getString(Constants.NOWPLAYING_FRAGMENT_ID, Constants.TIMBER3);

        Fragment fragment = NavigationUtils.getFragmentForNowplayingID(fragmentID);
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment).commit();

    }

    @StyleRes
    @Override
    public int getActivityTheme() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme", false) ? R.style.AppTheme_FullScreen_Dark : R.style.AppTheme_FullScreen_Light;

    }

    @Override
    public int getLightToolbarMode() {
        return Config.LIGHT_TOOLBAR_AUTO;
    }


    public int getToolbarColor() {
        return Color.TRANSPARENT;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PreferencesUtility.getInstance(this).didNowplayingThemeChanged()) {
            PreferencesUtility.getInstance(this).setNowPlayingThemeChanged(false);
            recreate();
        }
    }
}
