package bsoft.com.lib_gallery.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Adm on 8/10/2016.
 */
public class PhotoModel implements Cloneable, Parcelable {
    public static final Creator<PhotoModel> CREATOR = new Creator<PhotoModel>() {
        @Override
        public PhotoModel createFromParcel(Parcel in) {
            return new PhotoModel(in);
        }

        @Override
        public PhotoModel[] newArray(int size) {
            return new PhotoModel[size];
        }
    };
    public String imgPath = "";
    public String folderName = "";
    public boolean hasContent = true;
    public String orientation = "";

    public PhotoModel() {
    }

    protected PhotoModel(Parcel in) {
        imgPath = in.readString();
        folderName = in.readString();
        orientation = in.readString();
    }

    public PhotoModel copy() {
        PhotoModel model = new PhotoModel();
        model.imgPath = imgPath;
        model.folderName = folderName;
        model.hasContent = hasContent;
        model.orientation = orientation;
        return model;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgPath);
        dest.writeString(folderName);
        dest.writeString(orientation);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append("folderName: " + folderName);
        sb.append(", ");
        sb.append("imgPath: " + imgPath);
        sb.append(", ");
        sb.append("orientation: " + orientation);
        sb.append("]");
        return sb.toString();
    }
}
