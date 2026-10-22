package com.example.folioverse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {

    private EditText etUsername, etRole;
    private Button btnAddUser;
    private RecyclerView rvUsers;

    UserAdapter adapter;
    List<User> userList = new ArrayList<>();
    UserDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        etUsername = view.findViewById(R.id.etUsername);
        etRole = view.findViewById(R.id.etRole);
        btnAddUser = view.findViewById(R.id.btnAddUser);
        rvUsers = view.findViewById(R.id.rvUsers);

        // Initialize Room Database
        db = Room.databaseBuilder(
                requireContext(),
                UserDatabase.class,
                "user_db"
        ).allowMainThreadQueries().build();

        // Load users from database
        userList = db.userDao().getAllUsers();

        // Set up RecyclerView
        adapter = new UserAdapter(userList);
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvUsers.setAdapter(adapter);

        // Add user button click
        btnAddUser.setOnClickListener(v -> {
            String name = etUsername.getText().toString().trim();
            String role = etRole.getText().toString().trim();

            if (!name.isEmpty() && !role.isEmpty()) {
                User newUser = new User(name, role);
                db.userDao().insert(newUser);

                userList.clear();
                userList.addAll(db.userDao().getAllUsers());
                adapter.notifyDataSetChanged();

                etUsername.setText("");
                etRole.setText("");
            }
        });

        return view;
    }
}
