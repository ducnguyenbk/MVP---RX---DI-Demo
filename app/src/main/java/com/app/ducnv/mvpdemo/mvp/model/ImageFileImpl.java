package com.app.ducnv.mvpdemo.mvp.model;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by ducnv on 10/2/2017.
 */

public class ImageFileImpl implements ImagePath {

    File mImageFile;
    Context mContext;
    Uri mImageUri;

    @Override
    public void create(Context context, String filePath) throws IOException {
        mContext = context;
        mImageFile = new File(filePath);
    }

    @Override
    public void create(Context context, String name, String extension) throws IOException {
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        mImageFile = File.createTempFile(
                name,  /* prefix */
                extension,        /* suffix */
                storageDir      /* directory */
        );
    }

    @Override
    public String getPath() {
        if (mImageFile == null) {
            return "";
        }
        mImageUri = Uri.fromFile(mImageFile);
        return mImageUri.toString();
    }

    @Override
    public File getFile() {
        return mImageFile;
    }

    @Override
    public void setFile(File file) {
        mImageFile = file;
    }

    @Override
    public Uri getContentUri() {
        return mImageUri;
    }

    @Override
    public void setContentUri(Uri uri) {
        mImageUri = uri;
    }

    @Override
    public String toString() {
        if (mImageFile == null) return "";

        return  "File Name: " + mImageFile.getName() + "\n" +
                "File Path: " + mImageFile.getAbsolutePath();
//                "File URI: " + mImageUri;
    }

}
