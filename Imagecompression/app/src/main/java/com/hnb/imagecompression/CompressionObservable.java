package com.hnb.imagecompression;

import android.support.annotation.StringDef;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;
import java.util.StringTokenizer;

import rx.Observable;
import rx.Observer;

/**
 * Created by HuynhBinh on 6/15/16.
 */
public class CompressionObservable
{

    public static int COUNT = 0;

    public static Observable<String> compress(File folder)
    {
        File mFolder = folder;
        File[] files = mFolder.listFiles();

        return Observable.from(files).filter(file -> file.getName().contains("IMG")).skip(2).map(file -> Compression.compressImage(file, 50)).limit(3);

    }

    public static Observable<String> copy(File fromFolder, File toFolder)
    {

        File[] fromFiles = fromFolder.listFiles();
        File[] toFiles = toFolder.listFiles();

        return Observable.from(fromFiles).map(fromFile -> {
            if (fromFile.isFile())
            {
                int fromSize = Integer.parseInt(String.valueOf(fromFile.length() / 1024)); //in kilobyte

                String fromName = fromFile.getName();

                for(int i = 0; i < toFiles.length; i++)
                {
                    File toFile = toFiles[i];
                    if(fromName.equalsIgnoreCase(toFile.getName()))
                    {
                        int toSize = Integer.parseInt(String.valueOf(toFile.length() / 1024)); //in kilobyte

                        if(fromSize < toSize)
                        {
                            //copy file
                            try
                            {
                                FileUtils.copyFile(fromFile, toFile);
                            }
                            catch (Exception ex)
                            {

                            }

                        }

                    }
                }
            }

            return fromFile.getName();

        });
    }


    public static Observable<Integer> compress1(File folder)
    {

        File mFolder = folder;
        File[] files = mFolder.listFiles();

        //.filter(file1 -> file1.getName().startsWith("4993e04c"))

        return Observable.from(files).map(file -> {

            String resultUrl = "";

            if (file.isFile())
            {
                int fileSize = Integer.parseInt(String.valueOf(file.length() / 1024)); //in kilobyte

                String name = file.getName();

                if(fileSize > 800)
                {
                    resultUrl = Compression.compressImage(file.getAbsolutePath(), name, 60, Compression.SAVED_FORMAT);
                }
                else if (fileSize > 600)
                {
                    resultUrl = Compression.compressImage(file.getAbsolutePath(), name, 65, Compression.SAVED_FORMAT);
                }
                else if(fileSize > 400)
                {
                    resultUrl = Compression.compressImage(file.getAbsolutePath(), name, 75, Compression.SAVED_FORMAT);
                }
                else if (fileSize > 300)
                {
                    resultUrl = Compression.compressImage(file.getAbsolutePath(), name, 75, Compression.SAVED_FORMAT);
                }
                else if (fileSize > 250)
                {
                    resultUrl = Compression.compressImage(file.getAbsolutePath(), name, 85, Compression.SAVED_FORMAT);
                }
                else if (fileSize > 200)
                {
                    resultUrl = Compression.compressImage(file.getAbsolutePath(), name, 90, Compression.SAVED_FORMAT);
                }
                else if (fileSize > 150)
                {
                    resultUrl = Compression.compressImage(file.getAbsolutePath(), name, 90, Compression.SAVED_FORMAT);
                }
                else
                {
                    //resultUrl = Compression.compressImage(file.getAbsolutePath(), name, 90, Compression.SAVED_FORMAT);
                }

            }

            return COUNT++;

        });

    }

}
