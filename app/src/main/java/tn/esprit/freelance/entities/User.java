package tn.esprit.freelance.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "fullname")
    private String fullName;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "phonenumber")
    private String phoneNumber;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "confirm_password")
    private String confirmPassword;

    @ColumnInfo(name = "role")  // New field for user role
    private String role;  // Can be "ADMIN", "CLIENT", "FREELANCER"

    private boolean isBanned; // New field


    // New constructor to include 'role' field
    public User(String fullName, String email, String phoneNumber, String password, String confirmPassword, String role) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.role = role;
    }
    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }
    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean passwordsMatch() {
        return this.password.equals(this.confirmPassword);
    }

    @Override
    public String toString() {
        return fullName + " - " + email; // Adjust to display relevant info
    }

}
