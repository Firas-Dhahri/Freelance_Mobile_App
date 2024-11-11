package com.example.gestion_avis_et_evaluation;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestion_avis_et_evaluation.entity.Avis;
import com.example.gestion_avis_et_evaluation.model.ReviewViewModel;

import java.util.ArrayList;
import java.util.List;

public class ClientReviewsActivity extends AppCompatActivity {
    private RecyclerView rvReviews;
    private Button btnAddReview;
    private EditText etFilter;
    private ReviewsAdapter reviewsAdapter;
    private ReviewViewModel reviewViewModel; // Declaration for ViewModel

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients_review_list);

        rvReviews = findViewById(R.id.rvReviews);
        btnAddReview = findViewById(R.id.btnAddReview);
        etFilter = findViewById(R.id.etFilter);
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        reviewsAdapter = new ReviewsAdapter(new ArrayList<>());
        rvReviews.setAdapter(reviewsAdapter);

        // Initialize ViewModel
        reviewViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ReviewViewModel.class);

        // Observe LiveData from ViewModel
        reviewViewModel.getAllReviews().observe(this, reviews -> {
            reviewsAdapter.updateReviews(reviews);
        });

        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSubmitReviewActivity();
            }
        });
    }

    private void openSubmitReviewActivity() {
        Intent intent = new Intent(ClientReviewsActivity.this, SubmitReviewActivity.class);
        startActivity(intent);
    }
}