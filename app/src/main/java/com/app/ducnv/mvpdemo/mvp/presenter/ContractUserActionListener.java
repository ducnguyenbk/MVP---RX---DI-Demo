package com.app.ducnv.mvpdemo.mvp.presenter;

import android.content.Context;
import android.net.Uri;

/**
 * Created by ducnv on 10/2/2017.
 */

public interface ContractUserActionListener {
    void loadImage(Context context, Uri uri);

    void loadImageinfo();

    void copyImageIntoAppDir(Context context);

    void renameImage(Context context, String newName);

}
