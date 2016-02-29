package com.example.pratibha.whattocook;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class PlaySoundService1 extends Service {
    private MediaPlayer mp;
    public PlaySoundService1() {
    }

    @Override
    public void onCreate() {
        Log.v("Service1", "Inside Play Sound Service----------");

        mp = MediaPlayer.create(this, R.raw.soft_strings);
        mp.setLooping(true);
        mp.start();
    }

    @Override
    public void onDestroy(){
        //Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
        mp.stop();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
