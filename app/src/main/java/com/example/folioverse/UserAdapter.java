package com.example.folioverse;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    List<User> userList;

    public UserAdapter(List<User> users) {
        this.userList = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User u = userList.get(position);
        holder.tvName.setText(u.name);
        holder.tvRole.setText(u.role);

        // Delete user
        holder.btnDelete.setOnClickListener(v -> new AlertDialog.Builder(v.getContext())
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete " + u.name + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    UserDatabase db = Room.databaseBuilder(
                            v.getContext(),
                            UserDatabase.class,
                            "user_db"
                    ).allowMainThreadQueries().build();

                    db.userDao().delete(u); // delete from DB
                    userList.remove(position); // update UI
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, userList.size());
                })
                .setNegativeButton("No", null)
                .show());

        // Edit user
        holder.btnEdit.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Edit User");

            View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_edit_user, null);
            EditText etEditName = dialogView.findViewById(R.id.etEditName);
            EditText etEditRole = dialogView.findViewById(R.id.etEditRole);

            etEditName.setText(u.name);
            etEditRole.setText(u.role);

            builder.setView(dialogView);
            builder.setPositiveButton("Update", (dialog, which) -> {
                String newName = etEditName.getText().toString();
                String newRole = etEditRole.getText().toString();

                // Update object
                u.name = newName;
                u.role = newRole;

                // Update DB
                UserDatabase db = Room.databaseBuilder(
                        v.getContext(),
                        UserDatabase.class,
                        "user_db"
                ).allowMainThreadQueries().build();

                db.userDao().update(u); // persist edit
                notifyItemChanged(position); // update UI
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvRole;
        ImageView btnEdit, btnDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvRole = itemView.findViewById(R.id.tvRole);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
