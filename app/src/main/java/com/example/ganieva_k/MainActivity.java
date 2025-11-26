package com.example.ganieva_k;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvClock;
    private Button btnCreateRequest, btnRequestsList, btnStats;
    private final Handler clockHandler = new Handler(Looper.getMainLooper());

    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvClock = findViewById(R.id.tvClock);
        btnCreateRequest = findViewById(R.id.btn_create_request);
        btnRequestsList = findViewById(R.id.btn_requests_list);
        btnStats = findViewById(R.id.btn_stats);

        startClock();

        btnCreateRequest.setOnClickListener(v ->
                startActivity(new Intent(this, CreateRequestActivity.class)));
        btnRequestsList.setOnClickListener(v ->
                startActivity(new Intent(this, RequestListActivity.class)));
        btnStats.setOnClickListener(v ->
                startActivity(new Intent(this, StatsActivity.class)));
    }

    private void startClock() {
        clockHandler.post(new Runnable() {
            @Override
            public void run() {
                if (tvClock != null) {
                    String time = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
                    tvClock.setText(time);
                }
                clockHandler.postDelayed(this, 1000);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clockHandler.removeCallbacksAndMessages(null);
    }
}