package com.example.user.android_class_18;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private MediaPlayer mp;
    private Timer timer;



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mp = MediaPlayer.create(this,R.raw.musicmerio);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Boolean ispause = intent.getBooleanExtra("isPause",false);
        int seekto = intent.getIntExtra("seekto",-1);

        if(mp!=null && seekto >=0){
            mp.seekTo(seekto);

        }
        else if(mp!=null && !mp.isPlaying()){
            if(!ispause) {
                mp.start();

            }
        }else if (mp != null && mp.isPlaying()){
            if (ispause){
                mp.pause();

            }
        }


        int len = mp.getDuration();

        Intent it = new Intent("ppking");
        it.putExtra("len",len);
        sendBroadcast(it);

        timer = new Timer();
        timer.schedule(new timerTask(),0,500);

        return super.onStartCommand(intent, flags, startId);
    }

    private class timerTask extends TimerTask{
        @Override
        public void run() {
            if (mp !=null && mp.isPlaying()) {
                Intent it = new Intent("ppking");
                //目前的位置時間
                it.putExtra("now", mp.getCurrentPosition());
                sendBroadcast(it);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mp!=null){
//            if (mp.isPlaying()){
                mp.stop();
                Intent it = new Intent("ppking");
                it.putExtra("stop", 0);
                sendBroadcast(it);

//            }
            mp.release();
            mp = null;
        }

    }
}
