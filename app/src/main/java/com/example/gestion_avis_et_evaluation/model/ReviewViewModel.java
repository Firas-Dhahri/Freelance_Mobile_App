package com.example.gestion_avis_et_evaluation.model;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.gestion_avis_et_evaluation.dao.AvisDao;
import com.example.gestion_avis_et_evaluation.entity.Avis;

import java.util.List;

public class ReviewViewModel extends ViewModel {
    private final AvisDao avisDao;
    private final MutableLiveData<List<Avis>> allReviews;

    public ReviewViewModel(AvisDao avisDao) {
        this.avisDao = avisDao;
        allReviews = new MutableLiveData<>();
        loadData();
    }

    private void loadData() {
        new Thread(() -> {
            List<Avis> reviews = avisDao.getAllAvis();
            allReviews.postValue(reviews);
        }).start();
    }

    public LiveData<List<Avis>> getAllReviews() {
        return allReviews;
    }
}