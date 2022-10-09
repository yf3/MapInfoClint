package yf3.map_info;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import java.io.File;
import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
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
        gallery.setAdapter(new ImageAdapter(getAllShownImagesPath()));

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, final int position, long id) {
                Log.i("onItemClick", String.format("#%d Clicked!", position));
                final String filePath = (String) arg0.getAdapter().getItem(position);
                Navigation.findNavController(getView()).navigate(GalleryFragmentDirections.photoDetailAction(filePath));
            }
        });
    }

    private ArrayList<String> getAllShownImagesPath() {
        File[] internalStorageDirFiles = getContext().getFilesDir().listFiles();
        ArrayList<String> listOfAllImages = new ArrayList<>();
        for (File file: internalStorageDirFiles) {
            if (file.getName().endsWith(PhotoModel.PHOTO_EXT)) {
                listOfAllImages.add(file.getAbsolutePath());
            }
        }
        return listOfAllImages;
    }
}