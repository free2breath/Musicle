

package com.krintos.musicle.listeners;

/**
 * Listens for playback changes to send the the fragments bound to this activity
 */
public interface MusicStateListener {

    /**
     * Called when {@link com.krintos.musicle.MusicService#REFRESH} is invoked
     */
    void restartLoader();

    /**
     * Called when {@link com.krintos.musicle.MusicService#PLAYLIST_CHANGED} is invoked
     */
    void onPlaylistChanged();

    /**
     * Called when {@link com.krintos.musicle.MusicService#META_CHANGED} is invoked
     */
    void onMetaChanged();

}
