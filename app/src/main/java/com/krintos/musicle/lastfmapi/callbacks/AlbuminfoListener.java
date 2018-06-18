

package com.krintos.musicle.lastfmapi.callbacks;

import com.krintos.musicle.lastfmapi.models.LastfmAlbum;

public interface AlbuminfoListener {

    void albumInfoSucess(LastfmAlbum album);

    void albumInfoFailed();

}
