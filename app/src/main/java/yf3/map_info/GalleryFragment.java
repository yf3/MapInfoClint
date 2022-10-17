package yf3.map_info;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import yf3.map_info.databinding.GalleryFragmentBinding;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import java.io.File;
import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private GalleryFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = GalleryFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.galleryGridView.setAdapter(new ImageAdapter(getAllShownImagesPath()));
        binding.galleryGridView.setLayoutManager(new LinearLayoutManager(requireContext()));
        // TODO: adjust LayoutManager

//        binding.galleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View view, final int position, long id) {
//                Log.i("onItemClick", String.format("#%d Clicked!", position));
//                final String filePath = (String) arg0.getAdapter().getItem(position);
//                Navigation.findNavController(view).navigate(GalleryFragmentDirections.photoDetailAction(filePath));
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private ArrayList<String> getAllShownImagesPath() {
        File[] internalStorageDirFiles = requireContext().getFilesDir().listFiles();
        ArrayList<String> listOfAllImages = new ArrayList<>();
        for (File file: internalStorageDirFiles) {
            if (file.getName().endsWith(PhotoModel.PHOTO_EXT)) {
                listOfAllImages.add(file.getAbsolutePath());
            }
        }
        return listOfAllImages;
    }
}