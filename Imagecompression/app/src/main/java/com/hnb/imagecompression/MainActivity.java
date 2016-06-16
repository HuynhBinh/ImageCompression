package com.hnb.imagecompression;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import java.io.File;

import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
{

    Button button;
    TextView textView;

    // handler for received data from service
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equals(CompressionService.BROADCAST_ACTION))
            {
                final String param = intent.getStringExtra(CompressionService.PARAM);

                if(param.equalsIgnoreCase("done"))
                {
                    Intent intent1 = new Intent(MainActivity.this, CompressionService.class);
                    intent1.setAction(CompressionService.COMPRESS_ACTION);
                    getApplicationContext().stopService(intent1);
                }

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        textView.setText(param);
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(this);


        IntentFilter filter = new IntentFilter();
        filter.addAction(CompressionService.BROADCAST_ACTION);
        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(this);
        bm.registerReceiver(mBroadcastReceiver, filter);


        textView = (TextView) findViewById(R.id.txtResult);
        button = (Button) findViewById(R.id.btnStart);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                textView.setText("Loading");
                /*Intent intent = new Intent(MainActivity.this, CompressionService.class);
                intent.setAction(CompressionService.COMPRESS_ACTION);
                getApplicationContext().startService(intent);*/

            }
        });

        Observable clickObs = RxView.clicks(button).share();
        //clickObs.subscribe(view -> textView.setText("loading"));

        clickObs.subscribe(view -> compress());




    }


    public void compress()
    {
        String extr = Environment.getExternalStorageDirectory().toString();

        File mFolder = new File(extr + Compression.SOURCE_FOLDER);
        CompressionObservable.compress(mFolder)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(imageLink -> textView.setText(imageLink));
    }

    @Override
    protected void onDestroy()
    {
        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(this);
        bm.unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }


    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity)
    {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED)
        {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }
}
