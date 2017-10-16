package com.app.ducnv.mvpdemo.mvp.model;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.IOException;

/**
 * Created by ducnv on 10/2/2017.
 */

public interface ImagePath {
    void create(Context context, String filePath) throws IOException;

    void create(Context context, String name, String extension) throws IOException;

    String getPath();

    File getFile();

    void setFile(File file);

    //Content uri of file
    Uri getContentUri();

    void setContentUri(Uri uri);
}
