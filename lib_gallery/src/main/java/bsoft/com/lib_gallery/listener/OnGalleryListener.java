package bsoft.com.lib_gallery.listener;



import android.view.View;



import java.util.List;

import bsoft.com.lib_gallery.model.PhotoModel;

/**
 * Created by Adm on 8/10/2016.
 */
public interface OnGalleryListener {
    public void onGalleryLoaded(List<String> sortedFolderList);

    public void onPhotoPickListener(PhotoModel photoModel, View sourceView);
}
