package com.example.folioverse;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

public class AdminDashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Dashboard");

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null); // keep icon colors

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // ✅ Admin profile icon click
        View headerView = navigationView.getHeaderView(0);
        ImageView adminProfileIcon = headerView.findViewById(R.id.adminProfileIcon);
        adminProfileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, UserProfileActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        // ✅ Load DashboardFragment by default
        if (savedInstanceState == null) {
            loadFragment(new DashboardFragment(), "Dashboard");
            navigationView.setCheckedItem(R.id.nav_dashboard);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        String title = "";

        int id = item.getItemId();
        if (id == R.id.nav_dashboard) {
            selectedFragment = new DashboardFragment();
            title = "Dashboard";
        } else if (id == R.id.nav_manage_users) {
            selectedFragment = new UsersFragment();
            title = "Manage Users";
        } else if (id == R.id.nav_manage_books) {
            selectedFragment = new BooksFragment();
            title = "Manage Books";
        }

        if (selectedFragment != null) {
            loadFragment(selectedFragment, title);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFragment(Fragment fragment, String title) {
        toolbar.setTitle(title);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
    private void showExitConfirmationDialog()
    {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", (dialog, which) -> finishAffinity()) // Exit the app
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fm = getSupportFragmentManager();
            int count = fm.getBackStackEntryCount();
            if (count > 1) {
                fm.popBackStack();
            } else {
                Fragment current = fm.findFragmentById(R.id.fragment_container);
                if (current instanceof DashboardFragment) {
                    // 👇 Show confirmation dialog before exiting
                    showExitConfirmationDialog();
                } else {
                    super.onBackPressed();
                }
            }
        }
    }}