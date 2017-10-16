package com.app.ducnv.mvpdemo.mvp.view;

import android.net.Uri;

import com.app.ducnv.mvpdemo.mvp.presenter.ContractUserActionListener;


/**
 * Created by ducnv on 10/2/2017.
 */

public interface Contractview {
    void showImagePreview(Uri uri);

    void showImageInfo(String infoString);

    void setUserActionListener(ContractUserActionListener userActionListener);
}
