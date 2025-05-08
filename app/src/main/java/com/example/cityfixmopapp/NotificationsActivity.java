package com.example.cityfixmopapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        RecyclerView notificationsRecyclerView = findViewById(R.id.notificationsRecyclerView);

        // Sample data
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification("Your Hazard Reported", "your hazard 1 has been Inprogress."));
        notifications.add(new Notification("Profile Updated", "Your profile information has been successfully updated."));
        notifications.add(new Notification("Reminder", "Don't forget to check your notifications regularly."));

        // Set up the RecyclerView
        NotificationAdapter adapter = new NotificationAdapter(notifications);
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationsRecyclerView.setAdapter(adapter);
    }
}