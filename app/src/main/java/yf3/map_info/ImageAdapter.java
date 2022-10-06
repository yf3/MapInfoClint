package yf3.map_info;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private ArrayList<String> images;

    public ImageAdapter(ArrayList<String> allImagePath) {
        images = allImagePath;
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
            picturesView = new ImageView(parent.getContext());
            picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            picturesView.setLayoutParams(new GridView.LayoutParams(270, 270));

        } else {
            picturesView = (ImageView) convertView;
        }

        Glide.with(parent.getContext()).load(images.get(position)).apply(new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background).centerCrop())
                .into(picturesView);

        return picturesView;
    }
}