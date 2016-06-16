package com.hnb.imagecompression;

import android.util.Log;

import java.io.File;
import java.util.List;

import rx.Observable;
import rx.Observer;

/**
 * Created by HuynhBinh on 6/15/16.
 */
public class CompressionObservable
{

    public static Observable<String> compress(File folder)
    {
        File mFolder = folder;
        File[] files = mFolder.listFiles();

        return Observable.from(files)
                            .filter(file -> file.getName().contains("IMG")).skip(2)
                            .map(file -> Compression.compressImage(file, 50))
                            .limit(3);

    }

}
