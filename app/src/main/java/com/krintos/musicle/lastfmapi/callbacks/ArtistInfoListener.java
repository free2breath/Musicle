

package com.krintos.musicle.lastfmapi.callbacks;

import com.krintos.musicle.lastfmapi.models.LastfmArtist;

public interface ArtistInfoListener {

    void artistInfoSucess(LastfmArtist artist);

    void artistInfoFailed();

}
