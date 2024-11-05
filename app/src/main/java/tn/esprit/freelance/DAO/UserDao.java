package tn.esprit.freelance.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import tn.esprit.freelance.entities.User;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);
    @Query("SELECT * FROM user WHERE phoneNumber = :phoneNumber LIMIT 1")
    User findUserByPhoneNumber(String phoneNumber);

    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);



    @Query("SELECT * FROM user WHERE id = :userId LIMIT 1")
    User getUserById(long userId);
    // Insert a new user
    @Insert
    void createUser(User user);

    // Update an existing user
    @Update
    void updateUser(User user);

    // Delete a user
    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Query("SELECT * FROM user WHERE id = :id")
    User getUserById(Long id);

    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    User getUserByEmailAndPassword(String email, String password);

    @Query("UPDATE user SET id = :newId WHERE email = :email")
    void updateUserIdByEmail(String email, long newId);
}
