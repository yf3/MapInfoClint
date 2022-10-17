package yf3.map_info;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import yf3.map_info.databinding.AlbumItemBinding;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private final ArrayList<String> images;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private AlbumItemBinding mItemBinding;

        public ViewHolder(@NonNull AlbumItemBinding itemBinding) {
            super(itemBinding.getRoot());
            mItemBinding = itemBinding;
        }

        public void bind(String photoPath) {
            Glide.with(mItemBinding.getRoot().getContext())
                    .load(photoPath)
                    .apply(new RequestOptions().placeholder(R.drawable.ic_launcher_background).centerCrop())
                    .into(mItemBinding.mainImageView);
            mItemBinding.mainImageView.setOnClickListener(imageView ->
                Navigation.findNavController(mItemBinding.getRoot())
                        .navigate(GalleryFragmentDirections.photoDetailAction(photoPath)));
        }
    }

    public ImageAdapter(ArrayList<String> allImagePath) {
        images = allImagePath;
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AlbumItemBinding itemBinding = AlbumItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        // TODO: Glide Preload https://bumptech.github.io/glide/int/recyclerview.html
        String photoPath = images.get(position);
        holder.bind(photoPath);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    // ImageView.ScaleType.FIT_CENTER);
    // setLayoutParams(new GridView.LayoutParams(270, 270));

}