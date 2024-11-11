package com.example.gestion_avis_et_evaluation.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.gestion_avis_et_evaluation.dao.AvisDao;
import com.example.gestion_avis_et_evaluation.entity.Avis;

@Database(entities = {Avis.class}, version = 1)
public abstract  class AppDatabase extends RoomDatabase {
    public abstract AvisDao avisDao();
    private static AppDatabase instance;



    public static synchronized AppDatabase getDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "avis_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}