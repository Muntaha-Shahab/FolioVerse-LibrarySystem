package com.example.folioverse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

public class DashboardFragment extends Fragment {
    TextView tvTotalBooks, tvTotalUsers;
    BookDatabase bookDb;
    UserDatabase userDb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        tvTotalBooks = view.findViewById(R.id.tvTotalBooks);
        tvTotalUsers = view.findViewById(R.id.tvTotalUsers);

        bookDb = Room.databaseBuilder(requireContext(), BookDatabase.class, "book_db")
                .allowMainThreadQueries().build();

        userDb = Room.databaseBuilder(requireContext(), UserDatabase.class, "user_db")
                .allowMainThreadQueries().build();

        // Get counts and display
        int totalBooks = bookDb.bookDao().getAllBooks().size();
        int totalUsers = userDb.userDao().getAllUsers().size();

        tvTotalBooks.setText("Books: " + totalBooks);
        tvTotalUsers.setText("Users: " + totalUsers);

        return view;

    }
}

