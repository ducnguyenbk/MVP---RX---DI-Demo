package bsoft.com.lib_gallery.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bsoft.com.lib_gallery.R;
import bsoft.com.lib_gallery.model.PhotoModel;
import bsoft.com.lib_gallery.util.Statistic;

/**
 * Created by Adm on 8/10/2016.
 */
public class GalleryFooterAdapter extends RecyclerView.Adapter<GalleryFooterAdapter.ViewHolder> {
    private static final String TAG = GalleryFooterAdapter.class.getSimpleName();
    private final int itemCount;
    private int curIndex = 0;
    private Activity context = null;
    private List<PhotoModel> mList = new ArrayList<>();
    private OnClearImageChooseListener mOnClearImageChooseListener;

    public GalleryFooterAdapter(Activity activity, OnClearImageChooseListener listener) {
        this.context = activity;
        mOnClearImageChooseListener = listener;
        itemCount = activity.getIntent().getIntExtra(Statistic.EXTRA_NUM_OF_PHOTOS, 0);
        curIndex = activity.getIntent().getIntExtra(Statistic.EXTRA_CURRENT_INDEX_SELECTED, 0);
        ArrayList<PhotoModel> models = activity.getIntent().getParcelableArrayListExtra(Statistic.EXTRA_PHOTO_MODEL_LIST);

        Log.d("models ", " " + models.size());
        for (int i = 0; i < models.size(); i++) {

        }

        if (models.isEmpty()) {
            for (int i = 0; i < itemCount; i++) {
                mList.add(null);
            }
        } else {
            mList.addAll(models);
        }
        //   options = MyApplication.optionsCache();

    }

    public int getCurIndex() {
        return curIndex;
    }

    public String[] getOutput() {
        String[] output = new String[mList.size()];
        for (int i = 0; i < output.length; i++) {
            if (mList.get(i) == null) {
                output[i] = null;
            } else {
                output[i] = mList.get(i).imgPath;
            }
        }
        return output;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.gallery_item_photo_picked, null, false);

//        View view = LayoutInflater.from(context).inflate(R.layout.gallery_item_photo_picked, null, false);
        return new ViewHolder(itemView);
    }

    public void pushPhoto(PhotoModel model) {
        mList.set(curIndex, model);
        notifyItemChanged(curIndex);
        if (isFull()) return;
        do {
            curIndex++;
            if (curIndex >= mList.size()) curIndex = 0;
        } while (mList.get(curIndex) != null);
    }

    private boolean isFull() {
        for (PhotoModel model : mList) {
            if (model == null) return false;
        }
        return true;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (mList.get(position) != null) {
            if (true) {
                final ImageView imageView = holder.image;

//            Bitmap myBitmap = BitmapFactory.decodeFile(mList.get(position).imgPath);
//            imageView.setImageBitmap(myBitmap);

                Glide.with(context).load(new File(mList.get(position).imgPath))
//                        .asBitmap()
//                    .override(100, 100)
                        .error(R.drawable.ic_no_image)
                        .centerCrop()
                        .into(imageView);
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
//                        .into(new BitmapImageViewTarget(imageView) {
//                            @Override
//                            protected void setResource(Bitmap resource) {
//                                RoundedBitmapDrawable circularBitmapDrawable =
//                                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
//                                circularBitmapDrawable.setCornerRadius(10f);
//                                imageView.setImageDrawable(circularBitmapDrawable);
//                            }
//                        });
            } else {
                final ImageView imageView = holder.image;
                ImageLoader.getInstance().displayImage(String.valueOf("file:///" + mList.get(position).imgPath), imageView, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), loadedImage);
                        circularBitmapDrawable.setCornerRadius(10f);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
            }
        } else {
            holder.image.setImageResource(R.drawable.ic_gray);
        }

        if (curIndex == position) {
            holder.bg.setBackgroundResource(R.drawable.boder);
        } else {
            holder.bg.setBackgroundColor(Color.TRANSPARENT);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curIndex = position;
                notifyDataSetChanged();
            }
        });
        holder.clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("eee", "click close at pos = " + position);
                if (mList.get(position) != null) {
                    mList.set(position, null);
                    if (mOnClearImageChooseListener != null) {
                        mOnClearImageChooseListener.onClearImageClick();
                    }

                }
                curIndex = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    public ArrayList<PhotoModel> getChoosedModelList() {
        return (ArrayList<PhotoModel>) mList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public FrameLayout bg;
        public ImageView image;
        public View clearBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            bg = (FrameLayout) itemView.findViewById(R.id.fl_bg_preview);
            image = (ImageView) itemView.findViewById(R.id.img_picked);
            clearBtn = itemView.findViewById(R.id.clear_btn);
        }
    }

    public interface OnClearImageChooseListener {
        void onClearImageClick();
    }
}