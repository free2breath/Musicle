

package com.krintos.musicle.nowplaying;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.krintos.musicle.MusicPlayer;
import com.krintos.musicle.MusicService;
import com.krintos.musicle.R;
import com.krintos.musicle.utils.TimberUtils;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

public class Timber1 extends BaseNowplayingFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_timber1, container, false);

        setMusicStateListener();
        setSongDetails(rootView);

        return rootView;
    }

    @Override
    public void updateShuffleState() {
        if (shuffle != null && getActivity() != null) {
            MaterialDrawableBuilder builder = MaterialDrawableBuilder.with(getActivity())
                    .setIcon(MaterialDrawableBuilder.IconValue.SHUFFLE)
                    .setSizeDp(30);

            builder.setColor(TimberUtils.getBlackWhiteColor(accentColor));

            shuffle.setImageDrawable(builder.build());
            shuffle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MusicPlayer.setShuffleMode(MusicService.SHUFFLE_NORMAL);
                            MusicPlayer.next();
                            recyclerView.scrollToPosition(MusicPlayer.getQueuePosition());
                        }
                    }, 150);

                }
            });
        }
    }

}
