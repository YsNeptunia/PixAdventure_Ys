package com.example.first;

public class User {
    private String username;
    private int avatarResId;
    private float[] completionPercentages;
    private String[] mapData;

    public User(String username, int avatarResId, float[] completionPercentages, String[] mapData) {
        this.username = username;
        this.avatarResId = avatarResId;
        this.completionPercentages = completionPercentages;
        this.mapData = mapData;
    }

    // Getters and setters...
}