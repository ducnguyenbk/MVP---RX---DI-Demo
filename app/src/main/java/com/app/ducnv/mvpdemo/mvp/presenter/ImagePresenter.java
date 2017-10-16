package com.app.ducnv.mvpdemo.mvp.presenter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.app.ducnv.mvpdemo.mvp.model.ImagePath;
import com.app.ducnv.mvpdemo.mvp.presenter.ContractUserActionListener;
import com.app.ducnv.mvpdemo.mvp.view.Contractview;
import com.app.ducnv.mvpdemo.utils.Util;

import java.io.File;
import java.io.IOException;

/**
 * Created by ducnv on 10/2/2017.
 */

public class ImagePresenter implements ContractUserActionListener {

    private ImagePath mImagePath;
    private Contractview mContractview;


    public ImagePresenter(Contractview contractview, ImagePath imagePath) {
        mContractview = contractview;
        mImagePath = imagePath;
        mContractview.setUserActionListener(this);
    }

    @Override
    public void loadImage(Context context, Uri uri) {
        try {
            mImagePath.create(context, uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mImagePath.setContentUri(uri);
        mContractview.showImagePreview(uri);
    }

    @Override
    public void loadImageinfo() {
        String infoString = mImagePath.toString();
        mContractview.showImageInfo(infoString);
    }

    @Override
    public void copyImageIntoAppDir(Context context) {
//        File srcFile = new File(Util.getImageRealPathFromURI(context, mImagePath.getContentUri()));

        //Create a new File in app's directory
        try {
            mImagePath.create(context, "newFile", ".jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        File destFile = mImagePath.getFile();

//        try {
//            Util.copyFile(srcFile, destFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        String infoString = mImagePath.toString();

        Log.d("infoString ", " " + infoString);
        mContractview.showImageInfo(infoString);

    }

    @Override
    public void renameImage(Context context, String newName) {
        File srcFile = mImagePath.getFile();
        File renamedFile = Util.renameFile(srcFile, newName, ".jpg");

        mImagePath.setFile(renamedFile);

        String infoString = mImagePath.toString();

        mContractview.showImageInfo(infoString);
    }
}
