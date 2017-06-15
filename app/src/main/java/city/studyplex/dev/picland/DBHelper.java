package city.studyplex.dev.picland;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

import city.studyplex.dev.picland.util.PathHelper;

/**
 * Created by GM.PiratiK on 29.05.2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Picland";

    private static final String TABLE_ALBUMS = "Album";
    private static final String ALBUM_PATH = "path";
    private static final String ALBUM_NAME = "name";
    //private static final String ALBUM_HIDDEN = "hidden";
    //private static final String ALBUM_EXCULDED = "excluded";

    private static final String TABLE_PHOTOS = "Photo";
    private static final String PHOTO_PATH = "path";
    private static final String PHOTO_FOLDER_PATH = "folderpath";
    private static final String PHOTO_DATE_TAKEN = "datetaken";

    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ALBUMS_TABLE = "CREATE TABLE " + TABLE_ALBUMS + "("
                + ALBUM_PATH + " TEXT, " + ALBUM_NAME + " TEXT)";

        String CREATE_PHOTOS_TABLE = "CREATE TABLE " + TABLE_PHOTOS + "("
                + PHOTO_PATH + " TEXT, " + PHOTO_FOLDER_PATH + " TEXT, "
                + PHOTO_DATE_TAKEN + " TEXT)";

        db.execSQL(CREATE_PHOTOS_TABLE);
        db.execSQL(CREATE_ALBUMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALBUMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOS);
        onCreate(db);
    }

    /* Normal Albums */

    public ArrayList<Album> getAllAlbums() {
        ArrayList<Album> contactList = new ArrayList<Album>();
        String selectQuery = "SELECT  * FROM " + TABLE_ALBUMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                contactList.add(new Album(
                        PathHelper.quoteReverse(cursor.getString(0)),
                        PathHelper.quoteReverse(cursor.getString(1))));
            } while (cursor.moveToNext());
        }
        return contactList;
    }

    /* Albums functions */

    public ArrayList<Photo> getPhotos(String path) {
        ArrayList<Photo> contactList = new ArrayList<>();
        String selectQuery = "SELECT  " + PHOTO_PATH + ", " + PHOTO_DATE_TAKEN
                + " FROM " + TABLE_PHOTOS + " WHERE " + PHOTO_FOLDER_PATH
                + "='" + PathHelper.quoteReplace(path) + "' ORDER BY "
                + PHOTO_DATE_TAKEN + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            contactList.add(new Photo(
                    PathHelper.quoteReverse(cursor.getString(0)),
                    cursor.getString(1)
            ));
        }

        return contactList;
    }

    // call from MainActivity.loadAlbums();
    public void updatePhotos() {
        int photosCount = getPhotosCount(); //--> <--//
        int mediaCount = getMediastorePhotosCount(); //--> <--//

        if(photosCount != mediaCount) {
            if(photosCount < mediaCount) {
                try {
                    loadPhotosFrom(getPhotosCount()); //-->
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {

            }
        }
    }

    // call from DBHelper.updatePhotos();
    public int getPhotosCount() {
        int count;
        String countQuery = "SELECT  * FROM " + TABLE_PHOTOS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();
        return count; // return to DBHelper.updatePhotos();
    }

    // call from DBHelper.updatePhotos();
    public int getMediastorePhotosCount() {
        int count;
        String[] projection = new String[] {MediaStore.Images.Media._ID};
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(images, projection, null, null, null);
        count = cursor.getCount();
        cursor.close();
        return count; // return to DBHelper.updatePhotos();
    }

    // call from DBHelper.updatePhotos();
    public void loadPhotosFrom(int i) {
        String[] projection = new String[] {
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.DATA,
        };

        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(images, projection, null, null, null);

        if(cursor.moveToPosition(i)) {
            int pathColumn = cursor.getColumnIndex(
                    MediaStore.Images.Media.DATA);
            int dateColumn = cursor.getColumnIndex(
                    MediaStore.Images.Media.DATE_TAKEN);

            do {
                Photo photo = new Photo(
                        PathHelper.quoteReverse(cursor.getString(pathColumn)),
                        cursor.getString(dateColumn));
                addPhoto(photo);
                checkAlbum(photo.getFolderPath());
            } while(cursor.moveToNext());
        }
    }

    public void checkAlbum(String folderPath) {
        String countQuery = "SELECT  * FROM " + TABLE_ALBUMS
                + " WHERE " + ALBUM_PATH + "='"
                + PathHelper.quoteReplace(folderPath) + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if(cursor.getCount() == 0)
            addAlbum(new Album(folderPath));
        cursor.close();
    }

    void addPhoto(Photo photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PHOTO_FOLDER_PATH, PathHelper.quoteReplace(photo.getFolderPath()));
        values.put(PHOTO_PATH, PathHelper.quoteReplace(photo.getPath()));
        values.put(PHOTO_DATE_TAKEN, photo.getDateTaken());
        db.insert(TABLE_PHOTOS, null, values);
        db.close();
    }

    void addAlbum(Album album) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ALBUM_NAME, PathHelper.quoteReplace(album.getDisplayName()));
        values.put(ALBUM_PATH, PathHelper.quoteReplace(album.getPath()));
        db.insert(TABLE_ALBUMS, null, values);
        db.close();
    }

}
