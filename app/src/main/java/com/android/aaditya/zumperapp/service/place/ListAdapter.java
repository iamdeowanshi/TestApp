package com.android.aaditya.zumperapp.service.place;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.aaditya.zumperapp.Config;
import com.android.aaditya.zumperapp.ListActivity;
import com.android.aaditya.zumperapp.R;
import com.android.aaditya.zumperapp.model.Place;
import com.android.aaditya.zumperapp.module.details.ReviewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aaditya on 11/29/17.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

private Context context;
    private List<Place> places;

    public ListAdapter(Context context, List<Place> places) {
        this.context = context;
        this.places = places;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.icon)
        ImageView imageView;
        @BindView(R.id.name)
        TextView name;

        public ViewHolder(View pageView) {
            super(pageView);
            ButterKnife.bind(this, pageView);
        }

        public void bindData(int position) {
            name.setText(places.get(position).getName());

            Picasso.with(context).load(places.get(position).getIcon()).into(imageView);
        }

    }
}
