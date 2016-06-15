package com.hnb.imagecompression;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{

    Button button;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(this);

        textView = (TextView)findViewById(R.id.txtResult);
        button = (Button)findViewById(R.id.btnStart);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                textView.setText("Loading");
                Compression.compressThread(new Compression.DoneCallback()
                {
                    @Override
                    public void onFinish()
                    {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                textView.setText("Done");
                            }
                        });

                    }

                    @Override
                    public void onProgress(final int progress)
                    {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                textView.setText(progress+"");
                            }
                        });
                    }
                });

            }
        });



    }


    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
