package bsoft.com.lib_gallery.helper;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by vutha on 3/22/2017.
 */

public class BitmapHelper {
    public static Bitmap recycle(Bitmap bitmap) {
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            bitmap = null;
        }
        return null;
    }

    synchronized public static Bitmap loadBitmapFromAssets(Context context, String fullPath) {
        Bitmap frameBitmap = null;

        String imagePath = fullPath;
        AssetManager assetMng = context.getAssets();

        // Create an input stream to read from the asset folder
        InputStream is = null;
        try {
            is = assetMng.open(imagePath);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        //Get the texture from the Android resource directory
        //InputStream is = context.getResources().openRawResource(R.drawable.radiocd5);
        if (frameBitmap != null) {
            frameBitmap.recycle();
            frameBitmap = null;
        }
        try {
            //BitmapFactory is an Android graphics utility for images
            frameBitmap = BitmapFactory.decodeStream(is);

        } finally {
            //Always clear and close
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return frameBitmap;
    }
}
