package bsoft.com.lib_gallery.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.File;
import java.util.List;

import bsoft.com.lib_gallery.R;
import bsoft.com.lib_gallery.helper.GalleryHelper;
import bsoft.com.lib_gallery.listener.OnGalleryListener;
import bsoft.com.lib_gallery.model.PhotoModel;

/**
 * Created by Adm on 8/10/2016.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private static final String TAG = GalleryAdapter.class.getSimpleName();
    private Context context = null;
    private List<PhotoModel> mList;
    private OnGalleryListener listener = null;
    private int itemSize;

    public GalleryAdapter(Context context, List<PhotoModel> list) {
        this.context = context;
        this.mList = list;
        init();
    }

    private void init() {
        itemSize = context.getResources().getDisplayMetrics().widthPixels / GalleryHelper.NUMBER_GRID_CNT;
        //    options = MyApplication.optionsUnCache();
    }


    public GalleryAdapter setOnGalleryListener(OnGalleryListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_item_photo_list, null, false);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(itemSize, itemSize);
        view.setLayoutParams(params);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final PhotoModel photoModel = mList.get(position);
        if (photoModel == null)
            return;
        Glide.with(context).load(new File(photoModel.imgPath))
                .listener(new RequestListener<File, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, File model, Target<GlideDrawable> target, boolean isFirstResource) {
                        // todo log exception
                        photoModel.hasContent = false;
                        // important to return false so the error placeholder can be placed
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(GlideDrawable resource, File model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        photoModel.hasContent = true;
                        return false;
                    }
                })
                .error(R.drawable.ic_no_image)
                .dontAnimate()
                .into(holder.imgView);
//        ImageLoader.getInstance().displayImage(String.valueOf("file:///" + photoModel.imgPath), holder.imgView,options);
//        photoModel.hasContent = true;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    Log.d("setOnClickListener ", "111111111 ");
                    PhotoModel delegate = photoModel.copy();
                    if (delegate == null)
                        return;
                    if (delegate.hasContent)
                        listener.onPhotoPickListener(delegate, holder.itemView);
                    else showNoImageDialog();
                }
            }
        });
    }

    private void showNoImageDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setMessage(context.getString(R.string.inform_small_image));
        builder.setPositiveButton(context.getString(R.string.ok), null);
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView imgView;

        public ViewHolder(View itemView) {
            super(itemView);
            imgView = (AppCompatImageView) itemView.findViewById(R.id.img_picked);
        }
    }
}