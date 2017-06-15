package city.studyplex.dev.picland;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import city.studyplex.dev.picland.util.PathHelper;

/**
 * Created by GM.PiratiK on 28.05.2017.
 */

public class Album implements Parcelable {

    private String path = "";
    private String displayName;
    private ArrayList<Photo> photos;

    protected Album(Parcel in) {
        this.path = in.readString();
        this.displayName = in.readString();
        if(in.readByte() == 0x01) {
            photos = new ArrayList<Photo>();
            in.readList(photos, Photo.class.getClassLoader());
        }
        else {
            photos = null;
        }
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    public Album(String path) {
        this.path = path;
        this.photos = new ArrayList<Photo>();
        this.displayName = PathHelper.getAlbumName(path);
    }

    public Album(String path, String displayName) {
        this.path = path;
        this.displayName = displayName;
        this.photos = new ArrayList<Photo>();
    }

    public void setPath() {
        try {
            this.path = PathHelper.getImageFolderPath(photos.get(0).getPath());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public String getPathCoverAlbum() {
        if(photos.size() > 0)
            return "file://" + photos.get(0).getPath();
        else
            return "drawable://" + R.drawable.ic_empty;
    }

    public int getImagesCount () {
        return photos.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this.displayName);
        if(photos == null) {
            dest.writeByte((byte)(0x00));
        }
        else {
            dest.writeByte((byte)(0x01));
            dest.writeList(photos);
        }
    }

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }

    public String getPath() {
        return path;
    }

    public String getDisplayName() {
        return displayName;
    }
}
