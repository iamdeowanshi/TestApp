package com.android.aaditya.zumperapp.module.details;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.aaditya.zumperapp.Config;
import com.android.aaditya.zumperapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aaditya on 11/10/17.
 */

public class ImageAdapter extends PagerAdapter {

    private List<String> images;
    private Context context;
    private LayoutInflater layoutInflater;

    public ImageAdapter(Context context, List<String> images) {
        this.images = images;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View pageView = layoutInflater.inflate(R.layout.pager_item_view, view, false);

        ViewHolder viewHolder = new ViewHolder(pageView);
        viewHolder.bindData(position);

        view.addView(pageView, 0);

        return pageView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


    public class ViewHolder {
        @BindView(R.id.imageView)
        ImageView imageView;

        public ViewHolder(View pageView) {
            ButterKnife.bind(this, pageView);
        }

        public void bindData(int position) {
            String imageUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + images.get(position) + "&key=" + Config.GOOGLE_API_KEY;
            Picasso.with(context).load(imageUrl).placeholder(R.drawable.ic_place_holder).into(imageView);
        }

    }
}
