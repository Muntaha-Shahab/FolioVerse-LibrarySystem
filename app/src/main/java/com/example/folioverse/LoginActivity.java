package com.example.folioverse;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton, googleLogin;
    private TextView signupLink, forgotPasswordLink;

    private FirebaseAuth auth;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            checkUserProfile(currentUser.getUid());
        } else {
            setContentView(R.layout.activity_login);
            initializeUI();
        }
    }

    private void initializeUI() {
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        googleLogin = findViewById(R.id.google_login);
        signupLink = findViewById(R.id.signup_link);
        forgotPasswordLink = findViewById(R.id.forgot_password_link);

        auth = FirebaseAuth.getInstance();

        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                .setSupported(true)
                                .setServerClientId(getString(R.string.default_web_client_id))
                                .setFilterByAuthorizedAccounts(false)
                                .build()
                )
                .setAutoSelectEnabled(true)
                .build();

        loginButton.setOnClickListener(v -> emailLogin());

        googleLogin.setOnClickListener(v -> {
            oneTapClient.beginSignIn(signInRequest)
                    .addOnSuccessListener(result -> {
                        IntentSenderRequest intentSenderRequest =
                                new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                        googleLauncher.launch(intentSenderRequest);
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Google Sign-In failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });

        signupLink.setOnClickListener(v -> startActivity(new Intent(this, SignupActivity.class)));
        forgotPasswordLink.setOnClickListener(v -> startActivity(new Intent(this, ForgotPasswordActivity.class)));
    }

    private void emailLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Enter a valid email");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Enter your password");
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        checkUserProfile(user.getUid());
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private final ActivityResultLauncher<IntentSenderRequest> googleLauncher =
            registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    try {
                        SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                        String idToken = credential.getGoogleIdToken();

                        if (idToken != null) {
                            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                            auth.signInWithCredential(firebaseCredential)
                                    .addOnSuccessListener(authResult -> {
                                        FirebaseUser user = auth.getCurrentUser();
                                        if (user != null) {
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            String uid = user.getUid();

                                            db.collection("users").document(uid).get()
                                                    .addOnSuccessListener(documentSnapshot -> {
                                                        if (documentSnapshot.exists()) {
                                                            redirectToRoleBasedActivity(documentSnapshot);
                                                        } else {
                                                            // Create user doc with default 'user' role
                                                            String name = user.getDisplayName();
                                                            String email = user.getEmail();

                                                            Map<String, Object> newUser = new HashMap<>();
                                                            newUser.put("name", name);
                                                            newUser.put("email", email);
                                                            newUser.put("role", "user"); // default role

                                                            db.collection("users").document(uid).set(newUser)
                                                                    .addOnSuccessListener(unused -> {
                                                                        redirectToRoleBasedActivityFromRole("user");
                                                                    })
                                                                    .addOnFailureListener(e -> {
                                                                        Toast.makeText(this, "Failed to save user info", Toast.LENGTH_SHORT).show();
                                                                    });
                                                        }
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(this, "Error fetching user data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Firebase login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Google sign-in error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private void checkUserProfile(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        redirectToRoleBasedActivity(documentSnapshot);
                    } else {
                        Toast.makeText(this, "User role not found. Please sign up first.", Toast.LENGTH_LONG).show();
                        auth.signOut();
                        setContentView(R.layout.activity_login);
                        initializeUI();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error checking profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    setContentView(R.layout.activity_login);
                    initializeUI();
                });
    }

    private void redirectToRoleBasedActivity(DocumentSnapshot documentSnapshot) {
        String role = documentSnapshot.getString("role");
        redirectToRoleBasedActivityFromRole(role);
    }

    private void redirectToRoleBasedActivityFromRole(String role) {
        if ("admin".equalsIgnoreCase(role)) {
            startActivity(new Intent(this, AdminDashboardActivity.class));
        } else {
            startActivity(new Intent(this, HomeActivity.class));
        }
        finish();
    }
}
