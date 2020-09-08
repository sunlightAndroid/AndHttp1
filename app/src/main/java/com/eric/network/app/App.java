package com.eric.network.app;

import android.app.Application;

import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;

/**
 * <pre>
 *     author : eric
 *     time   : 2020/02/04
 *     desc   :
 *     version:
 * </pre>
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Album.initialize(AlbumConfig.newBuilder(this)
                .setAlbumLoader(new MediaLoader())
                .build());
    }
}
