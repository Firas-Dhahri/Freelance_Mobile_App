package com.example.gestion_avis_et_evaluation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SubmitReviewActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText etComment;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_submit_review); // Assurez-vous que le nom de votre fichier XML est correct

        // Initialisation des vues
        ratingBar = findViewById(R.id.ratingBar);
        etComment = findViewById(R.id.etComment);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Listener pour le bouton de soumission
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReview(); // Appeler la méthode de soumission
            }
        });
    }

    private void submitReview() {
        // Récupérer la note et le commentaire
        float rating = ratingBar.getRating();
        String comment = etComment.getText().toString().trim();

        if (comment.isEmpty()) {
            Toast.makeText(this, "Please enter a comment", Toast.LENGTH_SHORT).show();
            return;
        }

        // Implémentez la logique pour enregistrer l'avis ici (par exemple, dans une base de données)
        // Vous pouvez également renvoyer les données à MainActivity si nécessaire

        Toast.makeText(this, "Review submitted!", Toast.LENGTH_SHORT).show();
        finish(); // Fermez l'activité de soumission après la soumission
    }
}