package com.android.aaditya.zumperapp.module.details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.aaditya.zumperapp.R;
import com.android.aaditya.zumperapp.model.Review;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aaditya on 11/10/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context context;
    private List<Review> reviews;

    public ReviewAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bindViews(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.author_image)
        ImageView authorImage;
        @BindView(R.id.author_name)
        TextView authorName;
        @BindView(R.id.rating_bar)
        RatingBar ratingBar;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.description)
        TextView description;

        private ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        /**
         * Binding data to view for each review.
         * @param review
         */
        private void bindViews(Review review) {
            authorName.setText(review.getAuthor_name());
            ratingBar.setRating(review.getRating());
            description.setText(review.getText());
            time.setText(review.getRelative_time_description());
            Picasso.with(context).load(review.getProfile_photo_url()).into(authorImage);
        }
    }
}
