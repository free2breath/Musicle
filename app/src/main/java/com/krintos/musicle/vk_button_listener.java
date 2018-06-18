package com.krintos.musicle;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.krintos.musicle.activities.MainActivity;
import com.krintos.musicle.fragments.VK;



public class vk_button_listener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        RemoteViews r = VK.remoteViews;

        /*manager.cancel(intent.getExtras().getInt("idc"));*/
        String whichbtn = intent.getExtras().getString("whichkey");
        if(whichbtn.equals("playpause")){
            WebView WebView = VK.WebView;
            ImageButton vk = VK.play_vk;

            r.setImageViewResource(R.id.play_vk_notif,R.drawable.vk_notif_play);
            vk.setBackgroundResource(R.drawable.ic_play_white_36dp);
            WebView.loadUrl("javascript:(function(){" +
                    "l=document.getElementsByClassName('ai_play');" +
                    "e=document.createEvent('HTMLEvents');" +
                    "e.initEvent('click',true,true);" +
                    "l[0].dispatchEvent(e);" +
                    "})()");


            WebView.loadUrl("javascript:window.INTERFACES.processContent(document.getElementsByClassName('audio_item ai_current ai_playing')[0].innerText);");
            WebView.loadUrl("javascript:window.INTERFACE5.processContent(document.getElementsByClassName('ai_artist')[0].innerText);");
            WebView.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByClassName('ai_title')[0].innerText);");
            WebView.loadUrl("javascript:window.ALBUMPHOTO.processContent(document.getElementsByClassName('ai_play')[0].outerHTML);");
            WebView.loadUrl("javascript:(function(){"+
                    "l=document.getElementsByClassName('audio_item ai_current')[0].previousElementSibling;"+
                    "l.scrollIntoView(true);"+
                    "})()");
        }
       else if (whichbtn.equals("playnext")){
            WebView WebView = VK.WebView;

            WebView.loadUrl("javascript:(function(){"+
                    "l=document.getElementsByClassName('audio_item ai_current')[0].nextElementSibling;"+
                    "e=document.createEvent('HTMLEvents');"+
                    "e.initEvent('click',true,true);"+
                    "l.dispatchEvent(e);"+
                    "})()");

            WebView.loadUrl("javascript:window.INTERFACE5.processContent(document.getElementsByClassName('ai_artist')[0].innerText);");
            WebView.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByClassName('ai_title')[0].innerText);");
            WebView.loadUrl("javascript:window.ALBUMPHOTO.processContent(document.getElementsByClassName('ai_play')[0].outerHTML);");
            WebView.loadUrl("javascript:(function(){"+
                    "l=document.getElementsByClassName('audio_item ai_current')[0].previousElementSibling;"+
                    "l.scrollIntoView(true);"+
                    "})()");
        }
       else if (whichbtn.equals("playprev")){
            WebView WebView = VK.WebView;


            WebView.loadUrl("javascript:(function(){"+
                    "l=document.getElementsByClassName('audio_item ai_current')[0].previousElementSibling;"+
                    "e=document.createEvent('HTMLEvents');"+
                    "e.initEvent('click',true,true);"+
                    "l.dispatchEvent(e);"+
                    "})()");
            WebView.loadUrl("javascript:window.INTERFACE5.processContent(document.getElementsByClassName('ai_artist')[0].innerText);");
            WebView.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByClassName('ai_title')[0].innerText);");
            WebView.loadUrl("javascript:window.ALBUMPHOTO.processContent(document.getElementsByClassName('ai_play')[0].outerHTML);");
            WebView.loadUrl("javascript:(function(){"+
                    "l=document.getElementsByClassName('audio_item ai_current')[0].previousElementSibling;"+
                    "l.scrollIntoView(true);"+
                    "})()");
        }else if(whichbtn.equals("close")){
            manager.cancelAll();


            android.os.Process.killProcess(android.os.Process.myPid());
        }else if (whichbtn.equals("open")){
            Intent i = new Intent(context, com.krintos.musicle.activities.VK.class);
            i.setAction(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(i);
        }


    }

}
