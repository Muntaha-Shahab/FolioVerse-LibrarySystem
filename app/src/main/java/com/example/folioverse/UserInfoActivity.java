package com.example.folioverse;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserInfoActivity extends AppCompatActivity {

    private EditText emailInput, usernameInput, monthInput, dayInput, yearInput;
    private RadioGroup radioGroupGender;
    private RadioButton radioMale, radioFemale, radioOther;
    private Button continueButton;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String role = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        emailInput = findViewById(R.id.inputEmail);
        usernameInput = findViewById(R.id.inputUsername);
        monthInput = findViewById(R.id.inputMonth);
        dayInput = findViewById(R.id.inputDay);
        yearInput = findViewById(R.id.inputYear);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        radioOther = findViewById(R.id.radioOther);
        continueButton = findViewById(R.id.btnContinue);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        String passedRole = getIntent().getStringExtra("ROLE");
        if (!TextUtils.isEmpty(passedRole)) {
            role = passedRole;
        }

        if (auth.getCurrentUser() != null) {
            String email = auth.getCurrentUser().getEmail();
            emailInput.setText(email);
            emailInput.setEnabled(false);
        }

        continueButton.setOnClickListener(v -> saveUserInfo());
    }

    private void saveUserInfo() {
        String email = emailInput.getText().toString().trim();
        String username = usernameInput.getText().toString().trim();
        String monthStr = monthInput.getText().toString().trim();
        String dayStr = dayInput.getText().toString().trim();
        String yearStr = yearInput.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(monthStr) ||
                TextUtils.isEmpty(dayStr) || TextUtils.isEmpty(yearStr)) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int month, day, year;
        try {
            month = Integer.parseInt(monthStr);
            day = Integer.parseInt(dayStr);
            year = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
            return;
        }

        String gender;
        int selectedId = radioGroupGender.getCheckedRadioButtonId();
        if (selectedId == R.id.radioMale) {
            gender = "Male";
        } else if (selectedId == R.id.radioFemale) {
            gender = "Female";
        } else if (selectedId == R.id.radioOther) {
            gender = "Other";
        } else {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show();
            return;
        }

        String birthday = month + "/" + day + "/" + year;
        String uid = auth.getCurrentUser().getUid();

        UserProfile userProfile = new UserProfile(email, username, birthday, gender, role);

        db.collection("users").document(uid).set(userProfile)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Details saved successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserInfoActivity.this, RoleSelectionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to save: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    public static class UserProfile {
        public String email;
        public String username;
        public String birthday;
        public String gender;
        public String role;

        public UserProfile() {}

        public UserProfile(String email, String username, String birthday, String gender, String role) {
            this.email = email;
            this.username = username;
            this.birthday = birthday;
            this.gender = gender;
            this.role = role;
        }
    }
}
