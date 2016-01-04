package com.rdc.imageloader.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.rdc.imageloader.R;
import com.rdc.imageloader.config.DisplayConfig;
import com.rdc.imageloader.config.ImageLoaderConfig;
import com.rdc.imageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by blackwhite on 15-11-7.
 */
public class ImageAdapter extends BaseAdapter {

    private List<String> mUrlList;
    private Context mContext;
    private boolean mIsGridViewIdle = false;

    public ImageAdapter(Context mContext, List<String> mUrlList) {
        this.mContext = mContext;
        this.mUrlList = mUrlList;

        DisplayConfig displayConfig = new DisplayConfig.Builder()
                .imageLoadingResource(R.drawable.ic_file_download_blue_500_48dp)
                .imageFailResource(R.drawable.ic_error_blue_500_48dp)
                .imageEmptyResource(R.drawable.ic_error_blue_500_48dp)
                .build();
        ImageLoaderConfig imageLoaderConfig = new ImageLoaderConfig.Builder(mContext)
                .build();
        imageLoaderConfig.setDisplayConfig(displayConfig);


    }

    public void setGridViewScrollState(boolean isGridViewIdle) {
        mIsGridViewIdle = isGridViewIdle;
    }

    @Override
    public int getCount() {
        return mUrlList.size();
    }

    @Override
    public Object getItem(int position) {
        return mUrlList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item, null, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.item_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final String urlStr = mUrlList.get(position);
        if (mIsGridViewIdle) {
            /*ImageLoader.getInstance(mContext).displayImage(holder.imageView, mUrlList.get(position), new ImageLoader.ImageListener() {
                @Override
                public void onComplete(ImageView imageView, Bitmap bitmap) {
                    String tag = (String) imageView.getTag();
                    if(tag.equals(urlStr)) {
                        imageView.setImageBitmap(bitmap);
                    }else {
                        imageView.setImageResource(R.drawable.ic_refresh_blue_a400_18dp);
                    }
                }

                @Override
                public void onError(ImageView imageView) {
                    imageView.setImageResource(R.drawable.ic_error_blue_500_48dp);
                }
            });*/
            ImageLoader.getInstance().displayImage(holder.imageView, urlStr);
        }
        return convertView;
    }


    class ViewHolder {
        ImageView imageView;
    }
}
