package com.krintos.musicle.fragments;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.afollestad.appthemeengine.Config;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.krintos.musicle.R;
import com.krintos.musicle.utils.Helpers;

import java.security.PublicKey;

import static com.krintos.musicle.lastfmapi.models.LastfmArtist.ArtistTag.TAG;


public class vkdownloader extends Fragment {
    private ImageButton prev,play,next;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private int notification_id;
    private ProgressDialog pDialog;
    private RemoteViews remoteViews;
    private Context context;
    public AlertDialog alertDialog;
    public android.webkit.WebView WebView;
    AdView mAdView;
    String ateKey;
    int accentColor;
    public vkdownloader() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_vkdownloader, container, false);

        ateKey = Helpers.getATEKey(getActivity());
        accentColor = Config.primaryColor(getActivity(), ateKey);





        return rootView;
    }

}
