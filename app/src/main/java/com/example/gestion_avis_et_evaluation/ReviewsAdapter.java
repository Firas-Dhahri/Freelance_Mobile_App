package com.example.gestion_avis_et_evaluation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestion_avis_et_evaluation.entity.Avis;

import java.util.ArrayList;
import java.util.List;
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private List<Avis> reviews; // Update this to use the Avis model

    public ReviewsAdapter(List<Avis> reviews) {
        this.reviews = reviews != null ? reviews : new ArrayList<>(); // Initialize with an empty list if null
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_2, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.bind(reviews.get(position)); // Bind the data here
    }

    @Override
    public int getItemCount() {
        return reviews != null ? reviews.size() : 0;
    }

    public void updateReviews(List<Avis> newReviews) {
        this.reviews = newReviews;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewReviewerName; // TextView for reviewer name
        private RatingBar ratingBarReview; // RatingBar for review rating
        private TextView textViewReview; // TextView for review text

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewReviewerName = itemView.findViewById(R.id.tvReviewerName); // Replace with your TextView ID
            ratingBarReview = itemView.findViewById(R.id.ratingBarReview); // Replace with your RatingBar ID
            textViewReview = itemView.findViewById(R.id.tvReviewText); // Replace with your TextView ID
        }

        public void bind(Avis review) {
            textViewReviewerName.setText("Client ID: " + review.getClientId()); // Display client ID
            ratingBarReview.setRating(review.getRating());                       // Set the rating value
            textViewReview.setText(review.getComment());                         // Display the comment text
        }

    }
}