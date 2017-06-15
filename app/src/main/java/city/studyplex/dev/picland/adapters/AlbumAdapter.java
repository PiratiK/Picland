package city.studyplex.dev.picland.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import city.studyplex.dev.picland.Album;
import city.studyplex.dev.R;

/**
 * Created by GM.PiratiK on 29.05.2017.
 */

public class AlbumAdapter extends ArrayAdapter<Album> {

    private ArrayList<Album> albums;
    private Bitmap photo = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_launcher);
    private Context context;
    private int layout;

    public AlbumAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Album> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layout = resource;
        this.albums = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RelativeLayout card_layout;
        View view;
        ImageView picture;
        TextView name;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(this.layout, parent, false);
            view.setTag(R.id.picture, view.findViewById(R.id.picture));
            view.setTag(R.id.pictureText, view.findViewById(R.id.pictureText));
            view.setTag(R.id.layout_card_id, view.findViewById(R.id.layout_card_id));
        }
        else {
            view = convertView;
        }

        card_layout = (RelativeLayout) view.getTag(R.id.layout_card_id);
        picture = (ImageView) view.getTag(R.id.picture);
        name = (TextView)view.getTag(R.id.pictureText);

        Album album = albums.get(position);

        //??
        //picture.setTag(album.getPathCoverAlbum());

        Glide.with(this.context)
                .load(album.getPathCoverAlbum())
                .into(picture);

        name.setText(album.getDisplayName());

        card_layout.setBackgroundColor(context.getColor(R.color.defaultAlbum));

        return view;
    }
}
