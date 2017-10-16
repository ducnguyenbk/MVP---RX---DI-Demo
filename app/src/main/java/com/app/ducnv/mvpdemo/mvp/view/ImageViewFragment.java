package com.app.ducnv.mvpdemo.mvp.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.ducnv.mvpdemo.R;
import com.app.ducnv.mvpdemo.mvp.model.ImageFileImpl;
import com.app.ducnv.mvpdemo.mvp.presenter.ContractUserActionListener;
import com.app.ducnv.mvpdemo.mvp.presenter.ImagePresenter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class ImageViewFragment extends Fragment implements Contractview, View.OnClickListener {
    private ImageView mImage;
    private TextView mTvPath;
    private Button mBtnShowPath;
    private Button mBtnRenameFile;
    private Button mBtnChooseImage;
    public static final int SELECT_PICTURE = 2017;
    private ContractUserActionListener mContractUserActionListener;
    private String mNewFileName;

    public static ImageViewFragment newInstance() {
        ImageViewFragment fragment = new ImageViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContractUserActionListener = new ImagePresenter(this, new ImageFileImpl());
        initView();
    }

    private void initView() {
        mImage = (ImageView) getView().findViewById(R.id.image_demo);
        mTvPath = (TextView) getView().findViewById(R.id.tv_show_path);
        mBtnShowPath = (Button) getView().findViewById(R.id.btn_show_path);
        mBtnRenameFile = (Button) getView().findViewById(R.id.btn_rename_image);
        mBtnChooseImage = (Button) getView().findViewById(R.id.btn_choose_photo);
        mImage.setOnClickListener(this);
        mBtnShowPath.setOnClickListener(this);
        mBtnRenameFile.setOnClickListener(this);
        mBtnChooseImage.setOnClickListener(this);
    }

    @Override
    public void showImagePreview(Uri uri) {
        if (uri == null) return;
//        mImageThumbnail.setVisibility(View.VISIBLE);
        // This app uses Glide for image loading
        Glide.with(this)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(mImage);
    }

    @Override
    public void showImageInfo(String infoString) {
        Log.d("infoString ", " " + infoString);
        mTvPath.setText(infoString);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            mContractUserActionListener.loadImage(getActivity(), selectedImageUri);
            mContractUserActionListener.loadImageinfo();
        }
    }



    @Override
    public void setUserActionListener(ContractUserActionListener userActionListener) {
        mContractUserActionListener = userActionListener;
    }

    private void showImagePicker() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_choose_photo:
                showImagePicker();
                mBtnChooseImage.setVisibility(View.GONE);
                break;
            case R.id.btn_show_path:
                mTvPath.setVisibility(View.VISIBLE);
                mContractUserActionListener.copyImageIntoAppDir(getActivity());
                break;
            case R.id.btn_rename_image:
                showInputDialog();
                break;
        }

    }

    private void showInputDialog() {
        Log.d("showInputDialog ", "1111111  ");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter New Image Name");

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mNewFileName = input.getText().toString();
                //send request to rename file once new name is received.
                Log.d("mNewFileName ", " " + mNewFileName + "__" + getActivity() + "__" + mContractUserActionListener);
                mTvPath.setVisibility(View.VISIBLE);
                mContractUserActionListener.copyImageIntoAppDir(getActivity());
                mContractUserActionListener.renameImage(getActivity(), mNewFileName);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
