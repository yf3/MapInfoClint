package yf3.map_info;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import yf3.map_info.databinding.AlbumItemBinding;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private ArrayList<String> images;

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
            mItemBinding.mainImageView.setOnClickListener(imageView -> {
                Navigation
                    .findNavController(mItemBinding.getRoot())
                    .navigate(GalleryFragmentDirections.photoDetailAction(photoPath));
            });
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

    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView picturesView;
        if (convertView == null) {
            picturesView = new ImageView(parent.getContext());
            picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            picturesView.setLayoutParams(new GridView.LayoutParams(270, 270));

        } else {
            picturesView = (ImageView) convertView;
        }

        Glide.with(parent.getContext())
                .load(images.get(position))
                .apply(new RequestOptions().placeholder(R.drawable.ic_launcher_background).centerCrop())
                .into(picturesView);

        return picturesView;
    }
}