package tn.esprit.freelance.database;
import java.util.Date;
import android.content.Context;
import androidx.room.TypeConverters;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import android.util.Log;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import tn.esprit.freelance.DAO.ProjectDao;
import tn.esprit.freelance.DAO.UserDao;
import tn.esprit.freelance.entities.User;
import tn.esprit.freelance.entities.Project;
@Database(entities = {User.class,Project.class}, version = 4, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class ApplicationDatabase extends RoomDatabase {

    private static ApplicationDatabase instance;

    public abstract UserDao userDao();
    public abstract ProjectDao projectDao();
    // Define migration from version 1 to version 2
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Add the new 'role' column to the existing 'user' table
            database.execSQL("ALTER TABLE user ADD COLUMN role TEXT NOT NULL DEFAULT 'ADMIN'"); // Default to ADMIN or adjust as necessary
        }
    };
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Add a new column 'affiche' to the 'project' table
            database.execSQL("ALTER TABLE project ADD COLUMN affiche TEXT");
        }
    };
    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Step 1: Create a temporary table without the 'affiche' column
            database.execSQL("CREATE TABLE project_temp AS SELECT id, name, description, dateDeb, dateFin, status, owner, budget FROM project");

            // Step 2: Drop the old project table
            database.execSQL("DROP TABLE project");

            // Step 3: Rename the temporary table to 'project'
            database.execSQL("ALTER TABLE project_temp RENAME TO project");
        }
    };

    // Callback to insert an initial admin user when the database is created for the first time
    private static final RoomDatabase.Callback databaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.d("DatabaseCallback", "Database onCreate called"); // Add this log
            new Thread(() -> {
                UserDao userDao = instance.userDao();
                ProjectDao projectDao = instance.projectDao();
                User adminUser = new User("Admin", "aa@aa.com", "1234567890", "1111", "1111", "ADMIN");
                userDao.createUser(adminUser);

                Project initialProject = new Project(
                        "Project description", "Initial Project", new Date(), new Date(), "Open", "Admin", 1000.0);
                projectDao.create(initialProject);
            }).start();
        }
    };

    public static synchronized ApplicationDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ApplicationDatabase.class, "freelance")
                    .addMigrations(MIGRATION_1_2,MIGRATION_2_3,MIGRATION_3_4)  // Add the migration step
                    .addCallback(databaseCallback)  // Add the callback for initial admin user
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
