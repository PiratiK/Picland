package city.studyplex.dev.picland;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;

import city.studyplex.dev.R;
import city.studyplex.dev.picland.adapters.AlbumAdapter;
import city.studyplex.dev.picland.util.PathHelper;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle drawerToggle;
    private AlbumsHandler albums = new AlbumsHandler(MainActivity.this);
    private AlbumAdapter albumAdapter;
    private GridView albumGrid;
    private DBHelper dbHelper = new DBHelper(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_gallery_albums);

        //albums = new AlbumsHandler(MainActivity.this);
        //dbHelper = new DBHelper(MainActivity.this);

        initUiTweaks();
        checkPermissions(); //-->

    }

    public void initUiTweaks() {
        /* Change status bar color */
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.status_bar));

        /* Setup toolbars */
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        /* Get drawer items */
        String drawerArrayItems[] = getResources().getStringArray(R.array.drawer_items);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView drawerList = (ListView) findViewById(R.id.left_drawer);

        /* Drawer Switch or Toggle */
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        drawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.item_drawer, drawerArrayItems));

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                         /* Set gallery view */
                        albums.loadAlbums();
                        albumAdapter = new AlbumAdapter(MainActivity.this, R.layout.album_card, albums.getAlbums());
                        albumGrid = (GridView) findViewById(R.id.gridAlbums);
                        albumGrid.setAdapter(albumAdapter);
                        break;
                    case 1:
                         /* Set gallery view */
                        albums.loadAlbums();
                        albumAdapter = new AlbumAdapter(MainActivity.this, R.layout.album_card, albums.getAlbums());
                        albumGrid = (GridView) findViewById(R.id.gridAlbums);
                        albumGrid.setAdapter(albumAdapter);
                        break;
                    default: break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    // call from this.onCreate();
    public void checkPermissions() {
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.INTERNET))
                PathHelper.showToast(MainActivity.this, "Internet Access Denied!");
            else
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[] {Manifest.permission.INTERNET}, 1);
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))
                PathHelper.showToast(MainActivity.this, "no storage permission");
            else
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
        else
            loadAlbums(); //-->
    }

    // call from this.checkPermission();
    private void loadAlbums() {
        dbHelper.updatePhotos(); //-->
        albums.loadAlbums();

        albumAdapter = new AlbumAdapter(this, R.layout.album_card, albums.getAlbums());
        albumGrid = (GridView) findViewById(R.id.gridAlbums);
        albumGrid.setAdapter(albumAdapter);
        //albumGrid.setOnClickListener();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    loadAlbums();
                break;
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    PathHelper.showToast(MainActivity.this, "I got it from NET");
        }
    }
}
