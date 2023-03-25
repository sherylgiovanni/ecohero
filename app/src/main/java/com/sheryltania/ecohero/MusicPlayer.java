package com.sheryltania.ecohero;

import android.content.Context;
import android.media.MediaPlayer;
import com.sheryltania.ecohero.R;

public class MusicPlayer {

    private static MusicPlayer instance;
    private MediaPlayer mediaPlayer;

    private MusicPlayer(Context context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.music);
        mediaPlayer.setLooping(true);
    }

    public static MusicPlayer getInstance(Context context) {
        if (instance == null) {
            instance = new MusicPlayer(context);
        }
        return instance;
    }

    public void start() {
        mediaPlayer.start();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void release() {
        mediaPlayer.release();
        instance = null;
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

}