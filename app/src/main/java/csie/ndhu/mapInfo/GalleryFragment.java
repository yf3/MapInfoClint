package csie.ndhu.mapInfo;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
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

public class GalleryFragment extends Fragment {

    private GallerySharedViewModel mViewModel;

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    private class ImageAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<String> images;

        public ImageAdapter(Context localContext) {
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
            File[] internalStorageDirFiles = getContext().getFilesDir().listFiles();
            ArrayList<String> listOfAllImages = new ArrayList();
            for (File file: internalStorageDirFiles) {
                // TODO: only add jpeg
                listOfAllImages.add(file.getAbsolutePath());
            }
            return listOfAllImages;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.gallery_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GridView gallery = getView().findViewById(R.id.galleryGridView);

        gallery.setAdapter(new GalleryFragment.ImageAdapter(getContext()));

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, final int position, long id) {
                Log.i("onItemClick", String.format("#%d Clicked!", position));
                final String filePath = (String) arg0.getAdapter().getItem(position);
                GalleryDialogFragment fragment = GalleryDialogFragment.newInstance(filePath);
                fragment.show(getActivity().getSupportFragmentManager(), "dialog");

            }
        });

        mViewModel = new ViewModelProvider(this).get(GallerySharedViewModel.class);
        // TODO: Use the ViewModel
    }
}