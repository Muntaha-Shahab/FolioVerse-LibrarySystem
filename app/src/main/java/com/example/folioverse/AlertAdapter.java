package com.example.folioverse;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class AlertsAdapter extends RecyclerView.Adapter<AlertsAdapter.AlertViewHolder> {

    private final List<String> alertList;
    private final Context context;

    public AlertsAdapter(Context context, List<String> alertList) {
        this.context = context;
        this.alertList = alertList;
    }

    @NonNull
    @Override
    public AlertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_alert, parent, false);
        return new AlertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertViewHolder holder, int position) {
        String alert = alertList.get(position);
        holder.alertTitle.setText(alert);

        // ✅ Toast + AlertDialog on click
        holder.itemView.setOnClickListener(v -> {
            // Show AlertDialog (professional look)
            new AlertDialog.Builder(context)
                    .setTitle("FolioVerse Alert")
                    .setMessage(alert)
                    .setPositiveButton("OK", null)
                    .show();

            // Optional: Also show Toast
            Toast.makeText(context, "Clicked: " + alert, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return alertList.size();
    }

    static class AlertViewHolder extends RecyclerView.ViewHolder {
        TextView alertTitle;

        public AlertViewHolder(@NonNull View itemView) {
            super(itemView);
            alertTitle = itemView.findViewById(R.id.alertTitle);
        }
    }
}


