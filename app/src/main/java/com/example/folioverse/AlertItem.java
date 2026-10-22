package com.example.folioverse;

public class AlertItem {
    private int iconResId;
    private String title;
    private String description;
    private String time;
    private int thumbnailResId;

    public AlertItem(int iconResId, String title, String description, String time, int thumbnailResId) {
        this.iconResId = iconResId;
        this.title = title;
        this.description = description;
        this.time = time;
        this.thumbnailResId = thumbnailResId;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public int getThumbnailResId() {
        return thumbnailResId;
    }
}
