package com.example.folioverse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
public class BooksFragment extends Fragment {

    RecyclerView recyclerView;
    List<AdminBook> bookList = new ArrayList<>();
    BookAdapter adapter;
    BookDatabase db;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri selectedImageUri;
    private ImageView ivCoverPreview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books, container, false);

        recyclerView = view.findViewById(R.id.rvBooks);
        FloatingActionButton fab = view.findViewById(R.id.fabAddBook);

        // Initialize Room DB
        db = Room.databaseBuilder(
                requireContext(),
                BookDatabase.class,
                "book_db"
        ).allowMainThreadQueries().build();

        // Load books into bookList
        bookList.clear();
        bookList.addAll(db.bookDao().getAllBooks());

        // Set up RecyclerView
        adapter = new BookAdapter(bookList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            try {
                                requireContext().getContentResolver().takePersistableUriPermission(
                                        uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            } catch (SecurityException e) {
                                Log.e("BooksFragment", "Failed to take persistable URI permission", e);
                                Toast.makeText(getContext(), "Permission error!", Toast.LENGTH_SHORT).show();
                            }

                            selectedImageUri = uri;


                            if (ivCoverPreview != null) {
                                ivCoverPreview.setImageURI(uri);
                            }
                        }
                    }
                }
        );




        fab.setOnClickListener(v -> showAddBookDialog());

        return view;
    }
    private void showAddBookDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_book, null);
        EditText etTitle = dialogView.findViewById(R.id.etBookTitle);
        EditText etAuthor = dialogView.findViewById(R.id.etBookAuthor);
        EditText etGenre = dialogView.findViewById(R.id.etBookGenre);
        EditText etDescription = dialogView.findViewById(R.id.etBookDescription);
        ivCoverPreview = dialogView.findViewById(R.id.ivCoverPreview);

        Button btnSelectCover = dialogView.findViewById(R.id.btnSelectCover);

        btnSelectCover.setOnClickListener(view -> {
            Intent pickIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

            pickIntent.setType("image/*");
            imagePickerLauncher.launch(pickIntent);
        });

        new AlertDialog.Builder(getContext())
                .setTitle("Add New Book")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String title = etTitle.getText().toString().trim();
                    String author = etAuthor.getText().toString().trim();
                    String genre = etGenre.getText().toString().trim();
                    String description = etDescription.getText().toString().trim();

                    if (!title.isEmpty() && !author.isEmpty()) {

                        String coverUriStr = null;
                        if (selectedImageUri != null) {
                            requireContext().getContentResolver().takePersistableUriPermission(
                                    selectedImageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                            );
                            coverUriStr = selectedImageUri.toString();
                        }


                        AdminBook newBook = new AdminBook(title, author, genre, description, coverUriStr);
                        db.bookDao().insert(newBook);

                        bookList.clear();
                        bookList.addAll(db.bookDao().getAllBooks());
                        adapter.notifyDataSetChanged();
                        selectedImageUri = null;
                        ivCoverPreview = null;
                    } else {
                        Toast.makeText(getContext(), "Title and Author are required", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
