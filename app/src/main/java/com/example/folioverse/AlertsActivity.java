package com.example.folioverse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class AlertsActivity extends AppCompatActivity {

    private LinearLayout tabNotifications, tabMessages;
    private TextView tabTextNotifications, tabTextMessages;
    private View tabIndicatorNotifications, tabIndicatorMessages;

    private TextView noUpdatesText;
    private RecyclerView alertsRecycler;

    private AlertsAdapter alertsAdapter;
    private List<String> alertList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

        initViews();

        setupTabs();
        setupBottomNavigation();
        loadAlerts();  // Load alert data
    }

    private void initViews() {
        tabNotifications = findViewById(R.id.tabNotifications);
        tabMessages = findViewById(R.id.tabMessages);

        tabTextNotifications = findViewById(R.id.tabTextNotifications);
        tabTextMessages = findViewById(R.id.tabTextMessages);

        tabIndicatorNotifications = tabNotifications.getChildAt(1);
        tabIndicatorMessages = tabMessages.getChildAt(1);

        noUpdatesText = findViewById(R.id.noUpdatesText);
        alertsRecycler = findViewById(R.id.alertsRecycler);
        alertsRecycler.setLayoutManager(new LinearLayoutManager(this));
    }



    private void setupTabs() {
        tabNotifications.setOnClickListener(v -> showNotificationsTab());
        tabMessages.setOnClickListener(v -> showMessagesTab());
        showNotificationsTab(); // default
    }

    private void showNotificationsTab() {
        tabTextNotifications.setTextColor(ContextCompat.getColor(this, R.color.orange));
        tabTextMessages.setTextColor(ContextCompat.getColor(this, R.color.gray));

        tabIndicatorNotifications.setBackgroundColor(ContextCompat.getColor(this, R.color.orange));
        tabIndicatorMessages.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));

        loadAlerts();
    }

    private void showMessagesTab() {
        tabTextNotifications.setTextColor(ContextCompat.getColor(this, R.color.gray));
        tabTextMessages.setTextColor(ContextCompat.getColor(this, R.color.orange));

        tabIndicatorNotifications.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        tabIndicatorMessages.setBackgroundColor(ContextCompat.getColor(this, R.color.orange));

        showNoMessages();
    }

    private void loadAlerts() {
        alertList = new ArrayList<>();
        alertList.add(getString(R.string.alert_welcome));
        alertList.add(getString(R.string.alert_new_features));
        alertList.add(getString(R.string.alert_subscription_expiring));

        if (alertList.isEmpty()) {
            noUpdatesText.setVisibility(View.VISIBLE);
            alertsRecycler.setVisibility(View.GONE);
        } else {
            noUpdatesText.setVisibility(View.GONE);
            alertsRecycler.setVisibility(View.VISIBLE);
            alertsAdapter = new AlertsAdapter(this, alertList);
            alertsRecycler.setAdapter(alertsAdapter);
        }
    }

    private void showNoMessages() {
        noUpdatesText.setText(getString(R.string.no_messages_yet));
        noUpdatesText.setVisibility(View.VISIBLE);
        alertsRecycler.setVisibility(View.GONE);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.nav_notifications);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_search) {
                startActivity(new Intent(this, SearchActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, UserProfileActivity.class));
                return true;
            } else if (id == R.id.nav_notifications) {
                return true; // Already here
            }

            return false;
        });
    }
}
