package com.hnb.imagecompression;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by Huynh Binh PC on 6/14/2016.
 */
public class Compression
{

    public interface ProgressCallback
    {
        void onFinish();

        void onProgress(int progress);
    }

    public static Bitmap resizedBitmap;
    public static int MAX_WIDTH = 1200-1;
    public static String SOURCE_FOLDER = "/asd/dada";
    public static String DESTINATION_FOLDER = "/dada1";
    public static String SAVED_FORMAT = "jpg";


    public static void startCompress(final ProgressCallback callback)
    {
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                String extr = Environment.getExternalStorageDirectory().toString();

                File mFolder = new File(extr + SOURCE_FOLDER);

                compressImageInFolder(mFolder, callback);

            }
        };


        Thread thread = new Thread(runnable);
        thread.start();

    }

    public static void compressImageInFolder(File folder, ProgressCallback callback)
    {
        int count = 0;

        File mFolder = folder;

        for (File file : mFolder.listFiles())
        {
            if (file.isFile())
            {
                int fileSize = Integer.parseInt(String.valueOf(file.length() / 1024)); //in kilobyte

                String name = file.getName();

                if (fileSize > 600)
                {
                    compressImage(file.getAbsolutePath(), name, 65, SAVED_FORMAT);
                }
                else if (fileSize > 300)
                {
                    compressImage(file.getAbsolutePath(), name, 70, SAVED_FORMAT);
                }
                else if (fileSize > 250)
                {
                    compressImage(file.getAbsolutePath(), name, 70, SAVED_FORMAT);
                }
                else if (fileSize > 200)
                {
                    compressImage(file.getAbsolutePath(), name, 80, SAVED_FORMAT);
                }
                else if (fileSize > 150)
                {
                    compressImage(file.getAbsolutePath(), name, 80, SAVED_FORMAT);
                }
                else
                {
                    //compressImage(file.getAbsolutePath(), name, 100, SAVED_FORMAT);
                }


                /*else
                {
                    if(fileSize > 100)
                    {
                        //compress with 85%
                        compressImage(file.getAbsolutePath(), name, 85 , SAVED_FORMAT);
                    }
                    else
                    {
                        //no need to compress
                    }
                }*/

                callback.onProgress(count++);
            }
        }

        callback.onFinish();

    }

    public static String compressImage(File file, int quantity)
    {
        return compressImage(file.getAbsolutePath(), file.getName(), quantity, SAVED_FORMAT);
    }

    public static String compressImage(String inputImageLink, String outputImageName, int quantity, String savedFormat)
    {

        Bitmap bitmap = null;

        try
        {
            String extr = Environment.getExternalStorageDirectory().toString();

            //load image
            bitmap = BitmapFactory.decodeFile(inputImageLink);

            //save image back to sdcard
            File mFolder = new File(extr + DESTINATION_FOLDER);
            if (!mFolder.exists())
            {
                mFolder.mkdir();
            }

            String s = outputImageName.substring(0, outputImageName.indexOf('.')) + "." + savedFormat;
            File f = new File(mFolder.getAbsolutePath(), s);
            FileOutputStream fos = null;
            fos = new FileOutputStream(f);

            //if image size >
            //resize image by /2
            if (bitmap.getWidth() > MAX_WIDTH)
            {
                int desiredWidth = bitmap.getWidth();
                int desiredHeight = bitmap.getHeight();

                while (desiredWidth > MAX_WIDTH)
                {
                    desiredWidth = desiredWidth / 2;
                    desiredHeight = desiredHeight / 2;
                }

                resizedBitmap = Bitmap.createScaledBitmap(bitmap, desiredWidth, desiredHeight, false);
                resizedBitmap.compress(Bitmap.CompressFormat.WEBP, quantity, fos);

            }
            else
            {
                bitmap.compress(Bitmap.CompressFormat.WEBP, quantity, fos);
            }

            fos.flush();
            fos.close();


            return f.getAbsolutePath();
        }
        catch (Exception ex)
        {
            return ex.getMessage();
        }
        finally
        {
            if (bitmap != null)
            {
                bitmap.recycle();
            }
        }
    }
}
