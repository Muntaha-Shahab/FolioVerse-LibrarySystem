package com.example.folioverse;

public class UserProfile {
    private String email;
    private String username;
    private String birthday;
    private String gender;
    private String role; // 🔸 this exists but was missing getter/setter

    // Empty constructor needed for Firestore
    public UserProfile() {}

    public UserProfile(String email, String username, String birthday, String gender, String role) {
        this.email = email;
        setUsername(username); // enforce lowercase on save
        this.birthday = birthday;
        this.gender = gender;
        this.role = role;
    }

    // Getters and setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) {
        this.username = username != null ? username.toLowerCase() : null;
    }

    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    // 🔸 MISSING ACCESSORS FOR ROLE
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
