package com.example.folioverse;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView; // ✅ Added for title

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.ViewHolder> {

    private final List<Book> trendingBooks;
    private final Context context;

    public TrendingAdapter(Context context, List<Book> trendingBooks) {
        this.context = context;
        this.trendingBooks = trendingBooks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trending_book, parent, false); // make sure XML is updated
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = trendingBooks.get(position);
        holder.bookCover.setImageResource(book.getImageResId());

        // ✅ Set the title below the image
        holder.bookTitle.setText(book.getTitle());

        // ✅ Open BookDetailActivity with book data
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookDetailActivity.class);
            intent.putExtra("image", book.getImageResId());
            intent.putExtra("title", book.getTitle());
            intent.putExtra("author", book.getAuthor());
            intent.putExtra("genre", book.getGenre());
            intent.putExtra("description", book.getDescription());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return trendingBooks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookCover;
        TextView bookTitle; // ✅ Added TextView reference

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookCover = itemView.findViewById(R.id.trendingBookCover);
            bookTitle = itemView.findViewById(R.id.trendingBookTitle); // ✅ Initialize book title
        }
    }
}
