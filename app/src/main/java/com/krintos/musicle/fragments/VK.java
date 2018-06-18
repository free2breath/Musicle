package com.krintos.musicle.fragments;


import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.krintos.musicle.R;
import com.squareup.picasso.Picasso;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import org.apache.http.HttpHost;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * A simple {@link Fragment} subclass.
 */
public class VK extends Fragment {
    private NotificationCompat.Builder builder;
    public static NotificationManager notificationManager;
    public  int notification_id;
    private ProgressBar pDialog;
    public static RemoteViews remoteViews;
    private Context context;
    public FloatingActionButton quite, vk_timer, vk_options, refresh,vk_musicle,vk_proxy;
    public LinearLayout music, internetconnection;
    public static android.webkit.WebView WebView;
    public TextView vk_song_names,vk_song_names2;
    public static final String LOG_TAG = VK.class.getName();;
    public ProgressBar downloading;
    public String artist,song;
    private int progressStatus = 0;
    public int minute;
    String  ateKey;
    private InterstitialAd mInterstitialAd;
    public static File theDir;
    public  int showorhide;
    int accentColor;
    public String isitplaying;
    public int state;
    public ImageView albums_vk,retry;
    public static ImageButton prev_vk,play_vk,next_vk,download_vk;
    private ThinDownloadManager downloadManager;
    public VK() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_vk, container, false);
        vk_song_names = (TextView) rootView.findViewById(R.id.vk_song_names);
        vk_song_names2 = (TextView) rootView.findViewById(R.id.vk_song_names2);
        WebView = (WebView) rootView.findViewById(R.id.webview);
        albums_vk = (ImageView) rootView.findViewById(R.id.vk_photo);
        isitplaying="yes";
        showorhide = 1;
        WebView.setVisibility(View.GONE);
        downloading =(ProgressBar) rootView.findViewById(R.id.downloading);
        prev_vk = (ImageButton) rootView.findViewById(R.id.prev_vk);
        download_vk = (ImageButton) rootView.findViewById(R.id.download_vk);
        play_vk = (ImageButton) rootView.findViewById(R.id.play_vk);
        play_vk.setBackgroundResource(R.drawable.ic_play_white_36dp);
        next_vk = (ImageButton) rootView.findViewById(R.id.next_vk);
        music = (LinearLayout) rootView.findViewById(R.id.vk_music_player);
        music.setVisibility(View.GONE);
        context = getActivity();
        internetconnection = (LinearLayout) rootView.findViewById(R.id.internetconnection);
        internetconnection.setVisibility(View.GONE);
        retry=(ImageView) rootView.findViewById(R.id.retry);

        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        remoteViews = new RemoteViews(getActivity().getPackageName(), R.layout.vk_notification_builder);
        remoteViews.setImageViewResource(R.id.vk_photo_notif,R.drawable.vkonline);
        remoteViews.setImageViewResource(R.id.play_vk_notif,R.drawable.vk_notif_pause);
        notification_id = 321259683;
        final Intent button_intent = new Intent("button_clicked");
        button_intent.putExtra("whichkey","playpause");
        PendingIntent p_button_intent = PendingIntent.getBroadcast(getActivity().getApplicationContext(),3123,button_intent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.play_vk_notif,p_button_intent);

        Intent next_button = new Intent("next_button");
        next_button.putExtra("whichkey","playnext");
        PendingIntent next_play_p = PendingIntent.getBroadcast(getActivity().getApplicationContext(),3132,next_button,PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.next_vk_notif,next_play_p);

        final Intent prev_button = new Intent("prev_button");
        prev_button.putExtra("whichkey","playprev");
        PendingIntent prev_play_p = PendingIntent.getBroadcast(getActivity().getApplicationContext(),13323,prev_button,PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.prev_vk_notif,prev_play_p);

        Intent close_button = new Intent("close_button");
        close_button.putExtra("whichkey","close");
        PendingIntent close_button_p = PendingIntent.getBroadcast(getActivity().getApplicationContext(),312323,close_button,PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.close_notif,close_button_p);

        Intent open_button = new Intent("open_button");
        open_button.putExtra("whichkey","open");
        PendingIntent open_button_p = PendingIntent.getBroadcast(getActivity().getApplicationContext(),13342,open_button,PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.vk_notif_player,open_button_p);


        notificationManager.cancelAll();
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
            }
        });
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        assert connectivityManager != null;
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
        if (!connected){
            internetconnection.setVisibility(View.VISIBLE);

        }if (connected){
            class MyJavaScriptInterface
            {
                private TextView vk_song_names;
                public MyJavaScriptInterface(TextView aContentView)
                {

                    vk_song_names = aContentView;
                }

                @SuppressWarnings("unused")
                @JavascriptInterface
                public void processContent(String aContent)
                {
                    final String content = aContent;


                    vk_song_names.post(new Runnable()
                    {
                        public void run()
                        {
                            for (int i=0;i<3;i++){
                                notifrun();
                                remoteViews.setTextViewText(R.id.vk_song_names_notif1,content);
                            }
                            vk_song_names.setText(content);
                            artist = content;
                        }
                    });

                }
            }
            class MyJavaScriptInterface5
            {
                private TextView vk_song_names;
                public MyJavaScriptInterface5(TextView aContentView)
                {

                    vk_song_names = aContentView;
                }

                @SuppressWarnings("unused")
                @JavascriptInterface
                public void processContent(String aContent)
                {
                    final String content = aContent;


                    vk_song_names.post(new Runnable()
                    {
                        public void run()
                        {
                            for (int i=0;i<3;i++){
                                notifrun();
                                remoteViews.setTextViewText(R.id.vk_song_names_notif2,content);
                            }

                            vk_song_names2.setText(content);
                            song = content;
                        }
                    });

                }
            }
            class MyJavaScriptInterface1
            {
                private TextView vk_song_names;
                public MyJavaScriptInterface1(TextView aContentView)
                {

                    vk_song_names = aContentView;
                }

                @SuppressWarnings("unused")
                @JavascriptInterface
                public void processContent(String aContent)
                {


                    vk_song_names.post(new Runnable()
                    {
                        public void run()
                        {

                            play_vk.setBackgroundResource(R.drawable.ic_pause_white_36dp);

                            remoteViews.setImageViewResource(R.id.play_vk_notif,R.drawable.vk_notif_pause);


                        }
                    });

                }
            }

            class MyJavaScriptInterface3
            {
                private TextView vk_song_names;
                public MyJavaScriptInterface3(TextView aContentView)
                {

                    vk_song_names = aContentView;
                }

                @SuppressWarnings("unused")
                @JavascriptInterface
                public void processContent(String aContent)
                {
                    String newurl = aContent.substring(aContent.lastIndexOf("(https")+1);
                    final String lasturl= newurl.substring(0,newurl.indexOf("jpg")+3);
                    vk_song_names.post(new Runnable()
                    {
                        public void run()
                        {
                            loadalbumphoto(lasturl);
                        }
                    });

                }
            }
            class MyJavaScriptInterfaceLogout
            {
                private TextView vk_song_names;
                public MyJavaScriptInterfaceLogout(TextView aContentView)
                {

                    vk_song_names = aContentView;
                }

                @SuppressWarnings("unused")
                @JavascriptInterface
                public void processContent(String aContent)
                {
                    String getname = aContent.substring(aContent.lastIndexOf("name=")+6);
                    final String fullname= getname.substring(0,getname.indexOf("data")-2);

                    String newurl = aContent.substring(aContent.lastIndexOf("https:"));
                    final String lasturllast= newurl.substring(0,newurl.indexOf(".jpg")+4);
                    vk_song_names.post(new Runnable()
                    {
                        public void run()
                        {

                            /*Picasso.with(context).load(lasturllast).into(MainActivity.profilepic);
                            MainActivity.fullname.setText(fullname);*/
                        }
                    });

                }
            }class MyJavaScriptInterface6
            {
                private TextView vk_song_names;
                public MyJavaScriptInterface6(TextView aContentView)
                {

                    vk_song_names = aContentView;
                }

                @SuppressWarnings("unused")
                @JavascriptInterface
                public void processContent(String aContent)
                {
                    final String email = aContent;
                    vk_song_names.post(new Runnable()
                    {
                        public void run()
                        {
                            if (!email.equals("")) {
                                SharedPreferences warningversion = getActivity().getSharedPreferences("PREFS", 0);
                                SharedPreferences.Editor editor = warningversion.edit();
                                editor.putString("email", email);
                                editor.apply();
                            }
                        }
                    });

                }
            }class MyJavaScriptInterface7
            {
                private TextView vk_song_names;
                public MyJavaScriptInterface7(TextView aContentView)
                {

                    vk_song_names = aContentView;
                }

                @SuppressWarnings("unused")
                @JavascriptInterface
                public void processContent(String aContent)
                {
                    final String password = aContent;
                    vk_song_names.post(new Runnable()
                    {
                        public void run()
                        {
                            if (!password.equals("")) {
                                SharedPreferences warningversion = getActivity().getSharedPreferences("PREFS", 0);
                                SharedPreferences.Editor editor = warningversion.edit();
                                editor.putString("password", password);
                                editor.apply();
                            }
                        }
                    });

                }
            }

            String url = "https://m.vk.com/audios0?";
            WebView.getSettings().setJavaScriptEnabled(true);
            WebView.setWebViewClient(new WebViewClient());
            WebView.getSettings().setDomStorageEnabled(true);
            WebView.getSettings().setAppCacheEnabled(true);
            WebView.addJavascriptInterface(new MyJavaScriptInterface(vk_song_names), "INTERFACE");
            WebView.addJavascriptInterface(new MyJavaScriptInterface5(vk_song_names), "INTERFACE5");
            WebView.addJavascriptInterface(new MyJavaScriptInterface1(vk_song_names), "INTERFACES");
            WebView.addJavascriptInterface(new MyJavaScriptInterface3(vk_song_names),"ALBUMPHOTO");
            WebView.addJavascriptInterface(new MyJavaScriptInterface6(vk_song_names), "EMAIL");
            WebView.addJavascriptInterface(new MyJavaScriptInterface7(vk_song_names), "PASSWORD");

            WebView.addJavascriptInterface(new MyJavaScriptInterfaceLogout(vk_song_names), "PROFILEPIC");

            WebView.setWebChromeClient(new WebChromeClient());

            pDialog = (ProgressBar) rootView.findViewById(R.id.progressbar);

            SharedPreferences warningversionvk = getActivity().getSharedPreferences("PREFS", 0);
            String  shouldishowvk = warningversionvk.getString("closeforevervk", "");
            if (!shouldishowvk.equals("ofcourse")){
                final AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("REFRESH")
                        .setMessage(R.string.warning2)
                        .setPositiveButton(R.string.close1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton(R.string.close_forever,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences warningversion = getActivity().getSharedPreferences("PREFS", 0);
                                SharedPreferences.Editor editor = warningversion.edit();
                                editor.putString("closeforevervk", "ofcourse");
                                editor.apply();
                            }
                        })
                        .setIcon(R.drawable.refresh)
                        .show();
            }
            String energy_warning = warningversionvk.getString("energyvk","");
            if (!energy_warning.equals("nevershow")){
                AlertDialog.Builder energybuilder;
                energybuilder = new AlertDialog.Builder(getActivity());
                energybuilder.setTitle("WARNING")
                        .setMessage(R.string.energy_warning)
                        .setPositiveButton(R.string.close1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton(R.string.close_forever,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences warningversion = getActivity().getSharedPreferences("PREFS", 0);
                                SharedPreferences.Editor editor = warningversion.edit();
                                editor.putString("energyvk", "nevershow");
                                editor.apply();
                            }
                        })
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .show();
            }
            String proxy_warning = warningversionvk.getString("proxy","");
            switch (proxy_warning) {
                case "ukraine":
                    WebView webview = WebView;
                            /*String host = "51.15.129.248";
                            int port = 8080;*///france

                    String host = "188.130.138.69";
                    int port = 8080;
                    String applicationClassName = "android.app.Application";
                    setProxy(webview, host, port, applicationClassName);
                    break;
                case "noproxy":
                    String urlset = "https://m.vk.com/audios0?";
                    WebView.loadUrl(urlset);
                    break;
                default:
                    AlertDialog.Builder energybuilder;
                    energybuilder = new AlertDialog.Builder(getActivity());
                    energybuilder.setTitle("")
                            .setMessage(R.string.ukraine_user)
                            .setPositiveButton(R.string.ukraine_yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences warningversion = getActivity().getSharedPreferences("PREFS", 0);
                                    SharedPreferences.Editor editor = warningversion.edit();
                                    editor.putString("proxy", "ukraine");
                                    editor.apply();

                                    WebView webview = WebView;
                                /*String host = "51.15.129.248";
                                int port = 8080;*///france

                                    String host = "188.130.138.69";
                                    int port = 8080;
                                    String applicationClassName = "android.app.Application";
                                    setProxy(webview, host, port, applicationClassName);
                                }
                            })
                            .setNegativeButton(R.string.ukraine_no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences warningversion = getActivity().getSharedPreferences("PREFS", 0);
                                    SharedPreferences.Editor editor = warningversion.edit();
                                    editor.putString("proxy", "noproxy");
                                    editor.apply();
                                    String url = "https://m.vk.com/audios0?";

                                    WebView.loadUrl(url);
                                }
                            })
                            .setIcon(R.drawable.ukraine_user_verification)
                            .show();
                    break;
            }



        }
        prev_vk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playprevious();
            }
        });

        play_vk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playpause();
            }

        });

        next_vk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playnext();
            }
        });
        download_vk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences urltodownload = getActivity().getSharedPreferences("PREFS", 0);
                String url = urltodownload.getString("downloadurl","");
                downloadfromvk(url);
            }
        });


        return rootView;
    }


    private void color_picked(String picked_color) {
        SharedPreferences vk_colors = getActivity().getSharedPreferences("PREFS", 0);
        SharedPreferences.Editor editor = vk_colors.edit();
        editor.putString("picked_vk_color", picked_color);
        editor.apply();
        change_background();
    }

    public  void change_background() {
        SharedPreferences vk_colors = getActivity().getSharedPreferences("PREFS", 0);
        String  set_color = vk_colors.getString("picked_vk_color", "");
        if (!set_color.equals(0)){
            WebView.loadUrl("javascript:(function(){"+
                    "a=document.getElementsByClassName('pcont audioPage');"+
                    "a[0].style.backgroundColor=\""+set_color+" \";"+
                    "})()");
            WebView.loadUrl("javascript:(function(){"+
                    "a=document.getElementById('au_search_field');"+
                    "a.style.backgroundColor=\""+set_color+" \";"+
                    "})()");
        }
    }
    private void loadalbumphoto(String lasturl) {

        if (lasturl.substring(0, 1).equals("h")){
            Picasso.with(context).load(lasturl).into(albums_vk);
            albums_vk.buildDrawingCache();
            Bitmap bmap = albums_vk.getDrawingCache();
            remoteViews.setImageViewBitmap(R.id.vk_photo_notif,bmap);
        }else {
            albums_vk.setImageResource(R.drawable.vkonline);
            remoteViews.setImageViewResource(R.id.vk_photo_notif,R.drawable.vkonline);
        }
    }

    private void playprevious() {
        prev_vk.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.button_clicked));
        WebView.loadUrl("javascript:(function(){"+
                "l=document.getElementsByClassName('audio_item ai_current')[0].previousElementSibling;"+
                "e=document.createEvent('HTMLEvents');"+
                "e.initEvent('click',true,true);"+
                "l.dispatchEvent(e);"+
                "})()");
        WebView.loadUrl("javascript:window.INTERFACE5.processContent(document.getElementsByClassName('ai_artist')[0].innerText);");
        WebView.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByClassName('ai_title')[0].innerText);");
        WebView.loadUrl("javascript:window.ALBUMPHOTO.processContent(document.getElementsByClassName('ai_play')[0].outerHTML);");
        WebView.loadUrl("javascript:window.INTERFACES.processContent(document.getElementsByClassName('audio_item ai_current ai_playing')[0].innerText);");

        WebView.loadUrl("javascript:(function(){"+
                "l=document.getElementsByClassName('audio_item ai_current')[0].previousElementSibling;"+
                "l.scrollIntoView(true);"+
                "})()");
    }
    private void playnext() {

        next_vk.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.button_clicked));
        WebView.loadUrl("javascript:(function(){" +
                "l=document.getElementsByClassName('audio_item ai_current')[0].nextElementSibling;" +
                "e=document.createEvent('HTMLEvents');" +
                "e.initEvent('click',true,true);" +
                "l.dispatchEvent(e);" +
                "})()");

        WebView.loadUrl("javascript:window.INTERFACE5.processContent(document.getElementsByClassName('ai_artist')[0].innerText);");
        WebView.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByClassName('ai_title')[0].innerText);");
        WebView.loadUrl("javascript:window.ALBUMPHOTO.processContent(document.getElementsByClassName('ai_play')[0].outerHTML);");
        WebView.loadUrl("javascript:window.INTERFACES.processContent(document.getElementsByClassName('audio_item ai_current ai_playing')[0].innerText);");

        WebView.loadUrl("javascript:(function(){" +
                "l=document.getElementsByClassName('audio_item ai_current')[0].previousElementSibling;" +
                "l.scrollIntoView(true);" +
                "})()");

    }

    public void playpause() {
        play_vk.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.button_clicked));
        play_vk.setBackgroundResource(R.drawable.ic_play_white_36dp);
        remoteViews.setImageViewResource(R.id.play_vk_notif,R.drawable.vk_notif_play);
        WebView.loadUrl("javascript:(function(){"+
                "l=document.getElementsByClassName('ai_play');"+
                "e=document.createEvent('HTMLEvents');"+
                "e.initEvent('click',true,true);"+
                "l[0].dispatchEvent(e);"+
                "})()");

        WebView.loadUrl("javascript:window.INTERFACES.processContent(document.getElementsByClassName('audio_item ai_current ai_playing')[0].innerText);");
        WebView.loadUrl("javascript:window.INTERFACE5.processContent(document.getElementsByClassName('ai_artist')[0].innerText);");
        WebView.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByClassName('ai_title')[0].innerText);");
        WebView.loadUrl("javascript:window.ALBUMPHOTO.processContent(document.getElementsByClassName('ai_play')[0].outerHTML);");
        final Handler handler = new Handler();

        final int delay = 100;
        handler.postDelayed(new Runnable(){
            public void run(){
                WebView.loadUrl("javascript:window.INTERFACES.processContent(document.getElementsByClassName('audio_item ai_current ai_playing')[0].innerText);");
            }

        }, delay);
        WebView.loadUrl("javascript:(function(){"+
                "l=document.getElementsByClassName('audio_item ai_has_btn ai_current')[0].previousElementSibling;"+
                "l.scrollIntoView(true);"+
                "})()");
    }
    public class WebViewClient extends android.webkit.WebViewClient{
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            pDialog.setVisibility(View.VISIBLE);
            WebView.setVisibility(View.GONE);

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onLoadResource(WebView view, final String url) {
            change_background();

            WebView.loadUrl("javascript:window.EMAIL.processContent(document.getElementsByName('email')[0].value);");
            WebView.loadUrl("javascript:window.PASSWORD.processContent(document.getElementsByName('pass')[0].value);");

            WebView.loadUrl("javascript:window.INTERFACE5.processContent(document.getElementsByClassName('ai_artist')[0].innerText);");
            WebView.loadUrl("javascript:window.ALBUMPHOTO.processContent(document.getElementsByClassName('ai_play')[0].outerHTML);");
            WebView.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByClassName('ai_title')[0].innerText);");
            WebView.loadUrl("javascript:window.INTERFACES.processContent(document.getElementsByClassName('audio_item ai_current ai_playing')[0].innerText);");
            WebView.loadUrl("javascript:window.ALBUMPHOTO.processContent(document.getElementsByClassName('ai_play')[0].outerHTML);");
            if (url.contains(".mp3?extra")) {
                SharedPreferences urltodownload = getActivity().getSharedPreferences("PREFS", 0);
                SharedPreferences.Editor editor = urltodownload.edit();
                editor.putString("downloadurl",url);
                editor.apply();
            }
            super.onLoadResource(view, url);
        }

        /*@Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            WebView.setVisibility(View.GONE);
            internetconnection.setVisibility(View.VISIBLE);
            Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
            music.setVisibility(View.GONE);
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            Toast.makeText(context, ""+errorResponse, Toast.LENGTH_SHORT).show();

            WebView.setVisibility(View.GONE);
            internetconnection.setVisibility(View.VISIBLE);
            music.setVisibility(View.GONE);
            super.onReceivedHttpError(view, request, errorResponse);
        }*/

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;

        }
        @Override
        public void onPageFinished(WebView view, String url){
            autologin();
            WebView.loadUrl("javascript:window.PROFILEPIC.processContent(document.getElementsByClassName('ip_user_link')[0].innerHTML);");

            change_background();
            pDialog.setVisibility(View.INVISIBLE);
            music.setVisibility(View.VISIBLE);
            WebView.setVisibility(View.VISIBLE);


            super.onPageFinished(view,url);

        }

    }

    private void autologin() {

        SharedPreferences warningversion = getActivity().getSharedPreferences("PREFS", 0);
        String email = warningversion.getString("email","");
        String password = warningversion.getString("password","");
        if (!email.equals("")){
            WebView.loadUrl("javascript:(function(){"+
                    "l=document.getElementsByName('email');"+
                    "l[0].value=\""+email+"\";"+
                    "})()");
            WebView.loadUrl("javascript:(function(){"+
                    "l=document.getElementsByName('pass');"+
                    "l[0].value=\""+password+"\";"+
                    "})()");
        }
    }




    public void downloadfromvk(String url){


        theDir = new File("Musicle");

// if the directory does not exist, create it
        if (!theDir.exists()) {
            boolean result = false;

            try{
                theDir.mkdir();
                result = true;
            }
            catch(SecurityException se){
                //handle it
            }
            if(result) {
                Uri downloadUri = Uri.parse(url);
                Uri destinationUri = Uri.parse(Environment.getExternalStoragePublicDirectory(String.valueOf(theDir))+"/"+artist+" - "+song+".mp3");
                downloadManager = new ThinDownloadManager();
                DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                        .addCustomHeader("Auth-Token", "YourTokenApiKey")
                        .setRetryPolicy(new DefaultRetryPolicy())
                        .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.NORMAL)
                        .setDownloadListener(new DownloadStatusListener() {
                            @Override
                            public void onDownloadComplete(int id) {
                                downloading.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onDownloadFailed(int id, int errorCode, String errorMessage) {
                                Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onProgress(int id, long totalBytes, long downlaodedBytes, int progress) {
                                downloading.setVisibility(View.VISIBLE);
                                downloading.setMax(100);
                                downloading.setProgress(progress);
                            }
                        });
                downloadManager.add(downloadRequest);
            }
        }else {
            Uri downloadUri = Uri.parse(url);
            Uri destinationUri = Uri.parse(Environment.getExternalStoragePublicDirectory(String.valueOf(theDir))+"/"+artist+" - "+song+".mp3");
            downloadManager = new ThinDownloadManager();
            DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                    .addCustomHeader("Auth-Token", "YourTokenApiKey")
                    .setRetryPolicy(new DefaultRetryPolicy())
                    .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.NORMAL)
                    .setDownloadListener(new DownloadStatusListener() {
                        @Override
                        public void onDownloadComplete(int id) {
                            downloading.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onDownloadFailed(int id, int errorCode, String errorMessage) {
                            Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onProgress(int id, long totalBytes, long downlaodedBytes, int progress) {
                            downloading.setVisibility(View.VISIBLE);
                            downloading.setMax(100);
                            downloading.setProgress(progress);
                        }
                    });
            downloadManager.add(downloadRequest);
        }





    }
    public void notifrun(){
        Intent notification_intent = new Intent(context,com.krintos.musicle.activities.VK.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,notification_intent,0);
        builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.vknotifonline)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setContent(remoteViews)
                .setAutoCancel(false);
        notificationManager.notify(notification_id,builder.build());
    }
    public static boolean setProxy(WebView webview, String host, int port, String applicationClassName ) {
        // 3.2 (HC) or lower
        if (Build.VERSION.SDK_INT <= 13) {
            return setProxyUpToHC(webview, host, port);
        }
        // ICS: 4.0
        else if (Build.VERSION.SDK_INT <= 15) {
            return setProxyICS(webview, host, port);
        }
        // 4.1-4.3 (JB)
        else if (Build.VERSION.SDK_INT <= 18) {
            return setProxyJB(webview, host, port);
        }
        // 4.4 (KK) & 5.0 (Lollipop)
        else {
            return setProxyKKPlus(webview, host, port, applicationClassName);
        }
    }

    /**
     * Set Proxy for Android 3.2 and below.
     */
    @SuppressWarnings("all")
    private static boolean setProxyUpToHC(WebView webview, String host, int port) {
        Log.d(LOG_TAG, "Setting proxy with <= 3.2 API.");

        HttpHost proxyServer = new HttpHost(host, port);
        // Getting network
        Class networkClass = null;
        Object network = null;
        try {
            networkClass = Class.forName("android.webkit.Network");
            if (networkClass == null) {
                Log.e(LOG_TAG, "failed to get class for android.webkit.Network");
                return false;
            }
            Method getInstanceMethod = networkClass.getMethod("getInstance", Context.class);
            if (getInstanceMethod == null) {
                Log.e(LOG_TAG, "failed to get getInstance method");
            }
            network = getInstanceMethod.invoke(networkClass, new Object[]{webview.getContext()});
        } catch (Exception ex) {
            Log.e(LOG_TAG, "error getting network: " + ex);
            return false;
        }
        if (network == null) {
            Log.e(LOG_TAG, "error getting network: network is null");
            return false;
        }
        Object requestQueue = null;
        try {
            Field requestQueueField = networkClass
                    .getDeclaredField("mRequestQueue");
            requestQueue = getFieldValueSafely(requestQueueField, network);
        } catch (Exception ex) {
            Log.e(LOG_TAG, "error getting field value");
            return false;
        }
        if (requestQueue == null) {
            Log.e(LOG_TAG, "Request queue is null");
            return false;
        }
        Field proxyHostField = null;
        try {
            Class requestQueueClass = Class.forName("android.net.http.RequestQueue");
            proxyHostField = requestQueueClass
                    .getDeclaredField("mProxyHost");
        } catch (Exception ex) {
            Log.e(LOG_TAG, "error getting proxy host field");
            return false;
        }

        boolean temp = proxyHostField.isAccessible();
        try {
            proxyHostField.setAccessible(true);
            proxyHostField.set(requestQueue, proxyServer);
        } catch (Exception ex) {
            Log.e(LOG_TAG, "error setting proxy host");
        } finally {
            proxyHostField.setAccessible(temp);
        }
        WebView.loadUrl("https://m.vk.com/audios0?");

        Log.d(LOG_TAG, "Setting proxy with <= 3.2 API successful!");
        return true;
    }

    @SuppressWarnings("all")
    private static boolean setProxyICS(WebView webview, String host, int port) {
        try
        {
            Log.d(LOG_TAG, "Setting proxy with 4.0 API.");

            Class jwcjb = Class.forName("android.webkit.JWebCoreJavaBridge");
            Class params[] = new Class[1];
            params[0] = Class.forName("android.net.ProxyProperties");
            Method updateProxyInstance = jwcjb.getDeclaredMethod("updateProxy", params);

            Class wv = Class.forName("android.webkit.WebView");
            Field mWebViewCoreField = wv.getDeclaredField("mWebViewCore");
            Object mWebViewCoreFieldInstance = getFieldValueSafely(mWebViewCoreField, webview);

            Class wvc = Class.forName("android.webkit.WebViewCore");
            Field mBrowserFrameField = wvc.getDeclaredField("mBrowserFrame");
            Object mBrowserFrame = getFieldValueSafely(mBrowserFrameField, mWebViewCoreFieldInstance);

            Class bf = Class.forName("android.webkit.BrowserFrame");
            Field sJavaBridgeField = bf.getDeclaredField("sJavaBridge");
            Object sJavaBridge = getFieldValueSafely(sJavaBridgeField, mBrowserFrame);

            Class ppclass = Class.forName("android.net.ProxyProperties");
            Class pparams[] = new Class[3];
            pparams[0] = String.class;
            pparams[1] = int.class;
            pparams[2] = String.class;
            Constructor ppcont = ppclass.getConstructor(pparams);

            updateProxyInstance.invoke(sJavaBridge, ppcont.newInstance(host, port, null));
            WebView.loadUrl("https://m.vk.com/audios0?");

            Log.d(LOG_TAG, "Setting proxy with 4.0 API successful!");
            return true;
        }
        catch (Exception ex)
        {
            Log.e(LOG_TAG, "failed to set HTTP proxy: " + ex);
            return false;
        }
    }

    /**
     * Set Proxy for Android 4.1 - 4.3.
     */
    @SuppressWarnings("all")
    private static boolean setProxyJB(WebView webview, String host, int port) {
        Log.d(LOG_TAG, "Setting proxy with 4.1 - 4.3 API.");

        try {
            Class wvcClass = Class.forName("android.webkit.WebViewClassic");
            Class wvParams[] = new Class[1];
            wvParams[0] = Class.forName("android.webkit.WebView");
            Method fromWebView = wvcClass.getDeclaredMethod("fromWebView", wvParams);
            Object webViewClassic = fromWebView.invoke(null, webview);

            Class wv = Class.forName("android.webkit.WebViewClassic");
            Field mWebViewCoreField = wv.getDeclaredField("mWebViewCore");
            Object mWebViewCoreFieldInstance = getFieldValueSafely(mWebViewCoreField, webViewClassic);

            Class wvc = Class.forName("android.webkit.WebViewCore");
            Field mBrowserFrameField = wvc.getDeclaredField("mBrowserFrame");
            Object mBrowserFrame = getFieldValueSafely(mBrowserFrameField, mWebViewCoreFieldInstance);

            Class bf = Class.forName("android.webkit.BrowserFrame");
            Field sJavaBridgeField = bf.getDeclaredField("sJavaBridge");
            Object sJavaBridge = getFieldValueSafely(sJavaBridgeField, mBrowserFrame);

            Class ppclass = Class.forName("android.net.ProxyProperties");
            Class pparams[] = new Class[3];
            pparams[0] = String.class;
            pparams[1] = int.class;
            pparams[2] = String.class;
            Constructor ppcont = ppclass.getConstructor(pparams);

            Class jwcjb = Class.forName("android.webkit.JWebCoreJavaBridge");
            Class params[] = new Class[1];
            params[0] = Class.forName("android.net.ProxyProperties");
            Method updateProxyInstance = jwcjb.getDeclaredMethod("updateProxy", params);

            updateProxyInstance.invoke(sJavaBridge, ppcont.newInstance(host, port, null));
        } catch (Exception ex) {
            Log.e(LOG_TAG,"Setting proxy with >= 4.1 API failed with error: " + ex.getMessage());
            return false;
        }
        WebView.loadUrl("https://m.vk.com/audios0?");

        Log.d(LOG_TAG, "Setting proxy with 4.1 - 4.3 API successful!");
        return true;
    }

    // from https://stackoverflow.com/questions/19979578/android-webview-set-proxy-programatically-kitkat
    @SuppressLint("NewApi")
    @SuppressWarnings("all")
    private static boolean setProxyKKPlus(WebView webView, String host, int port, String applicationClassName) {
        Log.d(LOG_TAG, "Setting proxy with >= 4.4 API.");

        Context appContext = webView.getContext().getApplicationContext();
        System.setProperty("http.proxyHost", host);
        System.setProperty("http.proxyPort", port + "");
        System.setProperty("https.proxyHost", host);
        System.setProperty("https.proxyPort", port + "");
        try {
            Class applictionCls = Class.forName(applicationClassName);
            Field loadedApkField = applictionCls.getField("mLoadedApk");
            loadedApkField.setAccessible(true);
            Object loadedApk = loadedApkField.get(appContext);
            Class loadedApkCls = Class.forName("android.app.LoadedApk");
            Field receiversField = loadedApkCls.getDeclaredField("mReceivers");
            receiversField.setAccessible(true);
            ArrayMap receivers = (ArrayMap) receiversField.get(loadedApk);
            for (Object receiverMap : receivers.values()) {
                for (Object rec : ((ArrayMap) receiverMap).keySet()) {
                    Class clazz = rec.getClass();
                    if (clazz.getName().contains("ProxyChangeListener")) {
                        Method onReceiveMethod = clazz.getDeclaredMethod("onReceive", Context.class, Intent.class);
                        Intent intent = new Intent(Proxy.PROXY_CHANGE_ACTION);

                        onReceiveMethod.invoke(rec, appContext, intent);
                    }
                }
            }
            WebView.loadUrl("https://m.vk.com/audios0?");
            Log.d(LOG_TAG, "Setting proxy with >= 4.4 API successful!");
            return true;
        } catch (ClassNotFoundException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v(LOG_TAG, e.getMessage());
            Log.v(LOG_TAG, exceptionAsString);
        } catch (NoSuchFieldException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v(LOG_TAG, e.getMessage());
            Log.v(LOG_TAG, exceptionAsString);
        } catch (IllegalAccessException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v(LOG_TAG, e.getMessage());
            Log.v(LOG_TAG, exceptionAsString);
        } catch (IllegalArgumentException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v(LOG_TAG, e.getMessage());
            Log.v(LOG_TAG, exceptionAsString);
        } catch (NoSuchMethodException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v(LOG_TAG, e.getMessage());
            Log.v(LOG_TAG, exceptionAsString);
        } catch (InvocationTargetException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v(LOG_TAG, e.getMessage());
            Log.v(LOG_TAG, exceptionAsString);
        }
        return false;
    }

    private static Object getFieldValueSafely(Field field, Object classInstance) throws IllegalArgumentException, IllegalAccessException {
        boolean oldAccessibleValue = field.isAccessible();
        field.setAccessible(true);
        Object result = field.get(classInstance);
        field.setAccessible(oldAccessibleValue);
        return result;
    }



}
