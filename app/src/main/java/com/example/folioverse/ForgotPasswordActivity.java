package com.example.folioverse;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailInput;
    private Button resetButton, backButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password); // Use your layout

        emailInput = findViewById(R.id.email_input);
        resetButton = findViewById(R.id.reset_button);
        backButton = findViewById(R.id.btnBack);

        auth = FirebaseAuth.getInstance();

        resetButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.setError("Enter a valid email");
                return;
            }

            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        Toast.makeText(this, "If this email is registered, a reset link has been sent.", Toast.LENGTH_LONG).show();
                        finish(); // Optional: go back after sending
                    });
        });

        backButton.setOnClickListener(v -> finish());
    }
}
