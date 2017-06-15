package city.studyplex.dev.picland;

import android.os.Parcel;
import android.os.Parcelable;

import city.studyplex.dev.picland.util.PathHelper;

/**
 * Created by GM.PiratiK on 28.05.2017.
 */

public class Photo implements Parcelable {

    private String path;
    private String id;
    private String folderPath;

    private String dateTaken;

    protected Photo(Parcel in) {
        this.path = in.readString();
        this.id = in.readString();
        this.folderPath = in.readString();
        this.dateTaken = in.readString();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public Photo(String path) {
        this.path = path;
        this.folderPath = PathHelper.getImageFolderPath(path);
    }

    public Photo(String path, String dateTaken) {
        this.path = path;
        this.dateTaken = dateTaken;
        this.folderPath = PathHelper.getImageFolderPath(path);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this.id);
        dest.writeString(this.folderPath);
        dest.writeString(this.dateTaken);
    }

    public String getPath() {
        return this.path;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public String getDateTaken() {
        return dateTaken;
    }
}
