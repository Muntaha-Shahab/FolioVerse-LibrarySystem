package com.example.folioverse;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {

    private final Context context;
    private List<Book> storyList;

    public StoryAdapter(Context context, List<Book> storyList) {
        this.context = context;
        this.storyList = storyList;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_story_vertical, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        Book book = storyList.get(position);

        holder.bookImage.setImageResource(book.getImageResId());
        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());

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
        return storyList.size();
    }

    public void updateData(List<Book> filteredList) {
        this.storyList = filteredList;
        notifyDataSetChanged();
    }

    static class StoryViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImage;
        TextView title, author;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.storyCover);   // Make sure this ID matches your item_story.xml
            title = itemView.findViewById(R.id.storyTitle);
            author = itemView.findViewById(R.id.storyAuthor);
        }
    }
}
