package com.hnb.imagecompression;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by HuynhBinh on 6/15/16.
 */
public class CompressionService extends Service
{
    public static String COMPRESS_ACTION = "com.hnb.imagecompression.service.compress";

    public static String BROADCAST_ACTION = "com.hnb.imagecompression.service.broadcast";
    public static String PARAM = "progress";

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if(intent.getAction().equals(COMPRESS_ACTION))
        {
            Compression.startCompress(new Compression.ProgressCallback()
            {
                @Override
                public void onFinish()
                {
                    broadcastActivity("done");
                }

                @Override
                public void onProgress(final int progress)
                {
                    broadcastActivity(progress + "");
                }
            });

        }
        return START_STICKY;
    }

    // called to send data to Activity
    public void broadcastActivity(String param)
    {
        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra(PARAM, param);
        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(this);
        bm.sendBroadcast(intent);
    }
}
