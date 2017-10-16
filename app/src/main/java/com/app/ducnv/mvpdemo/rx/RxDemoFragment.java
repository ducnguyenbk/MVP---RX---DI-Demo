package com.app.ducnv.mvpdemo.rx;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.ducnv.mvpdemo.R;
import com.app.ducnv.mvpdemo.rx.adapter.ImageAdapter;
import com.app.ducnv.mvpdemo.utils.ResizeImage;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import bsoft.com.lib_gallery.GalleryActivity;
import bsoft.com.lib_gallery.model.PhotoModel;
import bsoft.com.lib_gallery.util.Statistic;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class RxDemoFragment extends Fragment implements View.OnClickListener {
    private TextView mTextView;
    private ArrayList<PhotoModel> mListPhotoModels = new ArrayList<>();
    public static final int REQ_PICK_IMAGE = 2017;
    private DisplayMetrics mDisplayMetrics;
    private ImageView mImageDemo;
    private List<String> mListPath = new ArrayList<>();
    private List<Bitmap> mListBitmapFinal = new ArrayList<>();
    public static final String TAG = RxDemoFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private ImageAdapter imageAdapter;


    public static RxDemoFragment newInstance() {
        RxDemoFragment fragment = new RxDemoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rx_demo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDisplayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);

        Log.d(TAG, " " + mDisplayMetrics.widthPixels);
        initView();
        initReycler();
    }

    private void initView() {
        mImageDemo = (ImageView) getView().findViewById(R.id.image);
        getView().findViewById(R.id.btn_click).setOnClickListener(this);
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycle_view);
    }

    private void initReycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        imageAdapter = new ImageAdapter(getActivity(), mListBitmapFinal);
        mRecyclerView.setAdapter(imageAdapter);

    }

    @Override
    public void onClick(View view) {
        openGallery(0, 9);
    }


    private void demoRx() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        /**
         *  Phát ra item
         */
        Observable.create(new ObservableOnSubscribe<List<Bitmap>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Bitmap>> e) throws Exception {
                e.onNext(getBitMapFromFilePath());
                e.onComplete();
            }
        })
                // run on a background thread
                /**
                 *
                 * immediate()   : Tạo ra và trả về 1 Scheduler để thực thi công việc trên thread hiện tại.
                 * computation() : hỗ trợ việc tính toán được hỗ trợ bởi 1 thread pool giới hạn với size bằng với số CPU hiện có.
                 * io()          : xử lý các công việc không mang nặng tính chất tính toán, được hỗ trợ bởi 1 thread pool không giới hạn có thể mở rộng khi cần
                 * newThread()   : tạo thread mới
                 * trampoline()  : sắp xếp 1 hàng chờ cho công việc trên thread hiện tại để thực thi khi công việc hiện tại kết thúc.
                 */
                .subscribeOn(Schedulers.computation())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())

                /**
                 * bắt đầu tực hiện cong việc
                 */
                .subscribe(new Observer<List<Bitmap>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Bitmap> list) {
                        mListBitmapFinal.addAll(list);
                        imageAdapter.notifyDataSetChanged();
                        // capnhatgiao
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        // Toat.thongbaoloi
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                        Log.d(TAG, "onComplete ");
                    }
                });


//        Observable.fromIterable(getBitMapFromFilePath())
//                .subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableObserver<Bitmap>() {
//                    @Override
//                    public void onNext(@NonNull Bitmap bitmap) {
//                        mListBitmapFinal.add(bitmap);
//                        imageAdapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d(TAG, "onComplete")
//                    }
//                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                getListImage(data);
            } else {
                getActivity().finish();
            }
        }
    }

    private List<Bitmap> getBitMapFromFilePath() {
        Bitmap bitmap = null;
        List<Bitmap> newList = new ArrayList<>();
        Log.d(TAG, "getBitMapFromFilePath " + mListPath.size());
        for (int i = 0; i < mListPath.size(); i++) {
            bitmap = new ResizeImage(getActivity()).getBitmap(mListPath.get(i), mDisplayMetrics.widthPixels);
            newList.add(bitmap);
        }
        return newList;
    }

    /**
     * param load data
     *
     * @return
     */
    private void getListImage(Intent data) {
        ArrayList<PhotoModel> models = data.getParcelableArrayListExtra(Statistic.EXTRA_CHANGED_PHOTOS);
        for (int i = 0; i < models.size(); i++) {
            PhotoModel photoModel = models.get(i);
            if (photoModel != null) {
                mListPath.add(photoModel.imgPath);
            }
        }
        demoRx();
    }

    private void openGallery(int curIndex, int coutList) {
        Intent galleryIntent = new Intent(getActivity(), GalleryActivity.class);
        galleryIntent.putExtra(Statistic.EXTRA_CURRENT_INDEX_SELECTED, curIndex);
        galleryIntent.putExtra(Statistic.EXTRA_NUM_OF_PHOTOS, coutList);
        if (curIndex >= 1 || curIndex < 0) return;
        if (mListPhotoModels.isEmpty()) {
            initPhotoModelList(coutList);
        }
        galleryIntent.putParcelableArrayListExtra(Statistic.EXTRA_PHOTO_MODEL_LIST, mListPhotoModels);
        startActivityForResult(galleryIntent, REQ_PICK_IMAGE);

    }

    private void initPhotoModelList(int numOfPhotos) {
        for (int i = 0; i < numOfPhotos; i++) {
            mListPhotoModels.add(null);
        }
    }

}
