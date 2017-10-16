package bsoft.com.lib_gallery.helper;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bsoft.com.lib_gallery.listener.OnGalleryListener;
import bsoft.com.lib_gallery.model.PhotoModel;
import bsoft.com.lib_gallery.util.FileUtil;

/**
 * Created by Adm on 4/3/2017.
 */
public class GalleryHelper {
    public static final int NUMBER_GRID_CNT = 4;
    private static final java.lang.String TAG = GalleryHelper.class.getSimpleName();
    public static List<PhotoModel> allPhotoList = null;
    public static Map<String, List<PhotoModel>> photoMap = null;
    public static List<String> sortedFolderList = null;
    public static List<PhotoModel> photoList = null;
    public static String currrentAlbum = "ALL";

    static {
        if (photoList == null) {
            photoList = new ArrayList<>();
        }

        if (allPhotoList == null) {
            allPhotoList = new ArrayList<>();
        }

        if (photoMap == null) {
            photoMap = new HashMap<>();
        }

        if (sortedFolderList == null) {
            sortedFolderList = new ArrayList<>();
        }
    }


    public static void loadGallery(final Activity activity, final OnGalleryListener onGalleryListener) {
        if (allPhotoList.size() <= 0 || allPhotoList.isEmpty()) {
            new AsyncTask<Void, Void, List<PhotoModel>>() {
                @Override
                protected List<PhotoModel> doInBackground(Void... params) {

                    List<PhotoModel> list = getAllShownImagesPath(activity);

                    Collections.sort(sortedFolderList, new Comparator<String>() {
                        @Override
                        public int compare(String lhs, String rhs) {
                            return lhs.compareTo(rhs);
                        }
                    });

//                    GalleryHelper.sortedFolderList.add(0, "ALL");

                    allPhotoList.addAll(list);

                    return list;
                }

                @Override
                protected void onPostExecute(List<PhotoModel> photoModels) {
                    super.onPostExecute(photoModels);

                    photoList.clear();
                    photoList.addAll(photoModels);
                    if (onGalleryListener != null) {
                        onGalleryListener.onGalleryLoaded(sortedFolderList);
                    }


                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            if (onGalleryListener != null) {
                onGalleryListener.onGalleryLoaded(sortedFolderList);
            }
        }
    }


    private static ArrayList<PhotoModel> getAllShownImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<PhotoModel> listOfAllImages = new ArrayList<PhotoModel>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATE_MODIFIED,
                MediaStore.Images.Media.ORIENTATION};

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
        if (cursor == null)
            return listOfAllImages;

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            PhotoModel model = new PhotoModel();
            absolutePathOfImage = cursor.getString(column_index_data);

            model.folderName = cursor.getString(column_index_folder_name);
            model.imgPath = absolutePathOfImage;
            model.orientation = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION));

            File tmpFile = new File(absolutePathOfImage);

            // khanh fix crash from developer console, check name != null before use
            if (tmpFile.exists() && tmpFile.getName() != null
                    && FileUtil.getExtension(tmpFile.getName()) != null
                    && !FileUtil.getExtension(tmpFile.getName()).equalsIgnoreCase("GIF")) {

            } else {
                continue;
            }


            List<PhotoModel> tmpList = photoMap.get(model.folderName);
            if (tmpList == null) {
                tmpList = new ArrayList<>();
                tmpList.add(model);
                photoMap.put(model.folderName, tmpList);

                sortedFolderList.add(model.folderName);

            } else {
                tmpList.add(model);
            }

            listOfAllImages.add(model);
        }
        if (cursor != null)
            cursor.close();
        return listOfAllImages;
    }

    public static void release() {
        if (GalleryHelper.allPhotoList != null) {
            GalleryHelper.allPhotoList.clear();
//            GalleryHelper.allPhotoList = null;
        }
        if (GalleryHelper.photoMap != null) {
            GalleryHelper.photoMap.clear();
//            GalleryHelper.photoMap = null;
        }
        if (GalleryHelper.sortedFolderList != null) {
            GalleryHelper.sortedFolderList.clear();
//            GalleryHelper.sortedFolderList = null;
        }
        if (GalleryHelper.photoList != null) {
            GalleryHelper.photoList.clear();
//            GalleryHelper.photoList = null;
        }
    }
}
