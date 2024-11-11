package com.example.gestion_avis_et_evaluation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestion_avis_et_evaluation.entity.Avis;
import com.example.gestion_avis_et_evaluation.model.ReviewViewModel;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAdminReviews;
    private Button btnFilterByDate;
    private ReviewsAdapter reviewsAdapter;
    private ReviewViewModel reviewViewModel; // Declaration for ViewModel

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        recyclerViewAdminReviews = findViewById(R.id.recyclerViewAdminReviews);
        btnFilterByDate = findViewById(R.id.btnFilterByDate);
        recyclerViewAdminReviews.setLayoutManager(new LinearLayoutManager(this));
        reviewsAdapter = new ReviewsAdapter(new ArrayList<>());
        recyclerViewAdminReviews.setAdapter(reviewsAdapter);

        // Initialize ViewModel
        reviewViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ReviewViewModel.class);

        // Observe LiveData from ViewModel
        reviewViewModel.getAllReviews().observe(this, reviews -> {
            reviewsAdapter.updateReviews(reviews);
        });

        btnFilterByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterReviewsByDate();
            }
        });

        // Note: The loadReviews method is now handled by LiveData
    }

    private void filterReviewsByDate() {
        // Implement the date filtering logic here
    }
}