package com.krintos.musicle.activities;

import android.animation.ValueAnimator;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.afollestad.appthemeengine.Config;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.krintos.musicle.R;
import com.krintos.musicle.fragments.vkdownloader;
import com.krintos.musicle.utils.Helpers;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class VK extends AppCompatActivity  {
    public Handler handler;
    String ateKey;
    public int delay = 10000; //milliseconds
    public View anim_view;
    int accentColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vk);
        ateKey = Helpers.getATEKey(this);
        accentColor = Config.accentColor(this, ateKey);
        anim_view =(View) findViewById(R.id.anim_view);
        GradientDrawable gd = (GradientDrawable) anim_view.getBackground();
        int width_px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
        gd.setStroke(width_px, accentColor);
        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(1000);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setRepeatMode(Animation.REVERSE);
        SharedPreferences warningversionvk = getSharedPreferences("PREFS", 0);
        String  edge_states = warningversionvk.getString("edge_state_on_off", "");
        FrameLayout edge_layout=(FrameLayout) findViewById(R.id.main);
        if (!edge_states.equals("edge_off")){
            int padding_in_dp = 3;  // 6 dps
            final float scale = getResources().getDisplayMetrics().density;
            int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
            assert edge_layout != null;
            edge_layout.setPadding(padding_in_px,padding_in_px,padding_in_px,padding_in_px);
            anim_view.setVisibility(View.VISIBLE);
            anim_view.startAnimation(anim);
        }

        final Fragment vk;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = this.getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.vk_bottom));
            vk = new com.krintos.musicle.fragments.VK();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.main,vk,"VK");
            ft.commit();
        }else{

            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(this);
            builder.setTitle("WARNING")
                    .setMessage(R.string.warning)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Fragment vk;
                            vk = new com.krintos.musicle.fragments.VK();
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.add(R.id.main,vk,"VK");
                            ft.commit();
                        }
                    })
                    .setNegativeButton(R.string.no,new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    })
                    .setIcon(R.drawable.ic_warning_black_24dp)
                    .show();
        }



     }
    @Override
    public void onResume()
    {  // After a pause OR at startup
        delay = 600000;
        super.onResume();


        //Refresh your stuff here
    }
    @Override
    public void onPause(){
        final Handler handler = new Handler();
        final WebView webView = com.krintos.musicle.fragments.VK.WebView;
        delay = 30000;
        SharedPreferences vk_colors = getSharedPreferences("PREFS", 0);
        String  set_color = vk_colors.getString("picked_vk_color", "");
        if (!set_color.equals(0)){
            webView.loadUrl("javascript:(function(){"+
                    "a=document.getElementsByClassName('pcont audioPage');"+
                    "a[0].style.backgroundColor=\""+set_color+" \";"+
                    "})()");
            webView.loadUrl("javascript:(function(){"+
                    "a=document.getElementById('au_search_field');"+
                    "a.style.backgroundColor=\""+set_color+" \";"+
                    "})()");
        }


        webView.loadUrl("javascript:(function(){"+
                "l=document.getElementsByClassName('audio_item ai_has_btn ai_current')[0].previousElementSibling;"+
                "l.scrollIntoView(true);"+
                "})()");
        handler.postDelayed(new Runnable(){
            public void run(){


                webView.loadUrl("javascript:(function(){"+
                        "l=document.getElementsByClassName('audio_item ai_has_btn ai_current')[0].previousElementSibling;"+
                        "l.scrollIntoView(true);"+
                        "})()");
                SharedPreferences vk_colors = getSharedPreferences("PREFS", 0);
                String  set_color = vk_colors.getString("picked_vk_color", "");
                if (!set_color.equals(0)){
                    webView.loadUrl("javascript:(function(){"+
                            "a=document.getElementsByClassName('pcont audioPage');"+
                            "a[0].style.backgroundColor=\""+set_color+" \";"+
                            "})()");
                    webView.loadUrl("javascript:(function(){"+
                            "a=document.getElementById('au_search_field');"+
                            "a.style.backgroundColor=\""+set_color+" \";"+
                            "})()");
                }


                handler.postDelayed(this, delay);
            }

        }, delay);
        super.onPause();
    }

    @Override
    public void onBackPressed(){

        moveTaskToBack(true);
    }



}
