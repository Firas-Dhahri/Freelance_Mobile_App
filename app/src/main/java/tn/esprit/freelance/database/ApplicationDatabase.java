package tn.esprit.freelance.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import tn.esprit.freelance.DAO.ApplicationDao;
import tn.esprit.freelance.DAO.UserDao;
import tn.esprit.freelance.entities.Application;
import tn.esprit.freelance.entities.User;

@Database(entities = {User.class, Application.class}, version = 3, exportSchema = false)
public abstract class ApplicationDatabase extends RoomDatabase {

    private static ApplicationDatabase instance;

    public abstract UserDao userDao();
    public abstract ApplicationDao applicationDao();
    // Define migration from version 1 to version 2
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
             // Create the new 'Applications' table
            database.execSQL("CREATE TABLE IF NOT EXISTS Applications ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + "nom TEXT, "
                    + "email TEXT, "
                    + "lettreMotivation TEXT, "
                    + "cvPath TEXT, "
                    + "date TEXT, "
                    + "status TEXT)");
        }
    };
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
           // Create the new 'Applications' table
            database.execSQL("CREATE TABLE IF NOT EXISTS Applications ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + "nom TEXT, "
                    + "email TEXT, "
                    + "lettreMotivation TEXT, "
                    + "cvPath TEXT, "
                    + "date TEXT, "
                    + "status TEXT)");
        }
    };

    // Callback to insert an initial admin user when the database is created for the first time
    private static final RoomDatabase.Callback databaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new Thread(() -> {
                UserDao userDao = instance.userDao();
                // Insert an initial admin user
                User adminUser = new User("Admin", "aa@aa.com", "1234567890", "1111", "1111", "ADMIN");
                userDao.createUser(adminUser);
            }).start();
        }
    };

    public static synchronized ApplicationDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ApplicationDatabase.class, "freelance")
                    .addMigrations(MIGRATION_1_2,MIGRATION_2_3)  // Add the migration step
                    .addCallback(databaseCallback)  // Add the callback for initial admin user
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
