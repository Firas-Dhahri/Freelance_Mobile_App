package com.example.gestion_avis_et_evaluation;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.example.gestion_avis_et_evaluation.dao.AvisDao;
import com.example.gestion_avis_et_evaluation.database.AppDatabase;
import com.example.gestion_avis_et_evaluation.entity.Avis;
import com.example.gestion_avis_et_evaluation.model.ReviewViewModel;

import java.util.ArrayList;
import java.util.List;
// Import these if not already included
import androidx.lifecycle.ViewModelProvider;

public class FreelancerReviewsActivity extends AppCompatActivity {

    private TextView tvAverageRating;
    private RecyclerView recyclerViewReviews;
    private ReviewsAdapter reviewsAdapter;
    private AppDatabase appDatabase;
    private ReviewViewModel reviewViewModel; // Declaration for ViewModel

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freelancer_reviews);

        tvAverageRating = findViewById(R.id.tvAverageRating);
        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        reviewsAdapter = new ReviewsAdapter(new ArrayList<>());
        recyclerViewReviews.setAdapter(reviewsAdapter);

        // Initialize the database
        appDatabase = AppDatabase.getDatabase(getApplicationContext());

        // Initialize ViewModel
        reviewViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ReviewViewModel.class);

        // Observe LiveData from ViewModel
        reviewViewModel.getAllReviews().observe(this, reviews -> {
            if (reviews.isEmpty()) {
                tvAverageRating.setText("Average Rating: 0.0");
            } else {
                double averageRating = calculateAverageRating(reviews);
                tvAverageRating.setText(String.format("Average Rating: %.1f", averageRating));
                reviewsAdapter.updateReviews(reviews); // Update the adapter with the actual reviews
            }
        });
    }

    private double calculateAverageRating(List<Avis> reviews) {
        int totalRating = 0;
        for (Avis review : reviews) {
            totalRating += review.getRating();
        }
        return (double) totalRating / reviews.size();
    }
}