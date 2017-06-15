package city.studyplex.dev.picland;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by GM.PiratiK on 28.05.2017.
 */

public class AlbumsHandler {
    private ArrayList<Album> albums;
    private Context context;
    //private ArrayList<Album> selectedAlbums;

    public AlbumsHandler(Context context) {
        this.context = context;
        this.albums = new ArrayList<Album>();
    }

    public void loadAlbums() {
        DBHelper db = new DBHelper(context);
        albums = db.getAllAlbums();
        for(Album album : albums)
            album.setPhotos(db.getPhotos(album.getPath()));
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }
}
