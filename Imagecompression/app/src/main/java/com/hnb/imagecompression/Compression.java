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

    public interface DoneCallback
    {
        void onFinish();
        void onProgress(int progress);
    }

    public static Bitmap resizedBitmap;
    public static int MAX_WIDTH = 1200;
    //public static int QUALITY = 70;


    public static void compressThread(final DoneCallback callback)
    {
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                int count = 0;
                String extr = Environment.getExternalStorageDirectory().toString();
                //save image back to sdcard
                File mFolder = new File(extr + "/asd/dibly.merchants");

                 mFolder.listFiles();

                for (File file : mFolder.listFiles())
                {
                    if (file.isFile())
                    {
                        int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
                        String name = file.getName();

                        if(file_size > 200)
                        {
                            compress(file.getAbsolutePath(), name, 70);
                        }
                        else
                        {

                            if(file_size > 100)
                            {
                                //compress with 100%
                                compress(file.getAbsolutePath(), name, 90);
                            }
                            else
                            {
                                //no need to compress
                            }
                        }

                        callback.onProgress(count++);
                    }
                }

                callback.onFinish();
            }
        };


        Thread thread = new Thread(runnable);
        thread.start();

    }

    public static boolean compress(String imageLink, String imageName, int quantity)
    {

        Bitmap bitmap = null;
        try
        {
            String extr = Environment.getExternalStorageDirectory().toString();

            //load image
            bitmap = BitmapFactory.decodeFile(imageLink);

            //save image back to sdcard
            File mFolder = new File(extr + "/ADirectory");
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }

            String s = imageName.substring(0, imageName.indexOf('.')) + ".jpg";
            File f = new File(mFolder.getAbsolutePath(), s);
            FileOutputStream fos = null;
            fos = new FileOutputStream(f);



            //if image size >
            //resize image by /2
            if (bitmap.getWidth() > MAX_WIDTH) {
                int desideWidth = bitmap.getWidth();
                int desideHeight = bitmap.getHeight();

                while (desideWidth > MAX_WIDTH) {
                    desideWidth = desideWidth / 2;
                    desideHeight = desideHeight / 2;
                }

                resizedBitmap = Bitmap.createScaledBitmap(bitmap, desideWidth, desideHeight, false);
                resizedBitmap.compress(Bitmap.CompressFormat.WEBP, quantity, fos);

            }
            else
            {
                bitmap.compress(Bitmap.CompressFormat.WEBP, quantity, fos);
            }
            fos.flush();
            fos.close();


            return true;
        }
        catch (Exception ex)
        {
            return false;
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
