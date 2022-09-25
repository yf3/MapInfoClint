package csie.ndhu.mapInfo;

import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;

// TODO: Convert to Fragment
// TODO: MVVM
public class Gallery extends AppCompatActivity implements GalleryDialogFragment.OnFragmentInteractionListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_sample);
        final GridView gallery = findViewById(R.id.galleryGridView);

        gallery.setAdapter(new ImageAdapter(Gallery.this));

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, final int position, long id) {
                Log.i("onItemClick", String.format("#%d Clicked!", position));
                final String filePath = (String) arg0.getAdapter().getItem(position);
                GalleryDialogFragment fragment = GalleryDialogFragment.newInstance(filePath);
                fragment.show(getSupportFragmentManager(), "dialog");

            }
        });

        gallery.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO: photo deleting feature
                return false;
            }
        });
    }

    @Override
    public void onFragmentInteraction() {

    }

    private class ImageAdapter extends BaseAdapter {
        private AppCompatActivity context;
        private ArrayList<String> images;

        public ImageAdapter(AppCompatActivity localContext) {
            context = localContext;
            images = getAllShownImagesPath();
        }

        public int getCount() {
            return images.size();
        }

        @Override
        public String getItem(int position) {
            return images.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ImageView picturesView;
            if (convertView == null) {
                picturesView = new ImageView(context);
                picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                picturesView.setLayoutParams(new GridView.LayoutParams(270, 270));

            } else {
                picturesView = (ImageView) convertView;
            }

            Glide.with(context).load(images.get(position)).apply(new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background).centerCrop())
                    .into(picturesView);

            return picturesView;
        }

        private ArrayList<String> getAllShownImagesPath() {
            File[] internalStorageDirFiles = getApplicationContext().getFilesDir().listFiles();
            ArrayList<String> listOfAllImages = new ArrayList();
            for (File file: internalStorageDirFiles) {
                // TODO: only add jpeg
                listOfAllImages.add(file.getAbsolutePath());
            }
            return listOfAllImages;
        }
    }
}
