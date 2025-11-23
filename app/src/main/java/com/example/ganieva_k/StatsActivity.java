package com.example.ganieva_k;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StatsActivity extends AppCompatActivity {

    private TextView
            tvTechTotal, tvTechWork, tvTechDone,
            tvPCTotal, tvPCWork, tvPCDone,
            tvMasterTotal, tvMasterWork, tvMasterDone;

    private RequestRepo repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        repo = new RequestRepo(this);

        tvTechTotal = findViewById(R.id.tvTechTotal);
        tvTechWork = findViewById(R.id.tvTechWork);
        tvTechDone = findViewById(R.id.tvTechDone);

        tvPCTotal = findViewById(R.id.tvPCTotal);
        tvPCWork = findViewById(R.id.tvPCWork);
        tvPCDone = findViewById(R.id.tvPCDone);

        tvMasterTotal = findViewById(R.id.tvMasterTotal);
        tvMasterWork = findViewById(R.id.tvMasterWork);
        tvMasterDone = findViewById(R.id.tvMasterDone);

        refreshStats();
    }

    private void refreshStats() {
        tvTechTotal.setText(String.valueOf(repo.countByCategory("TECH")));
        tvTechWork.setText(String.valueOf(repo.countByCategoryAndStatus("TECH", "IN_PROGRESS")));
        tvTechDone.setText(String.valueOf(repo.countByCategoryAndStatus("TECH", "DONE")));

        tvPCTotal.setText(String.valueOf(repo.countByCategory("PC")));
        tvPCWork.setText(String.valueOf(repo.countByCategoryAndStatus("PC", "IN_PROGRESS")));
        tvPCDone.setText(String.valueOf(repo.countByCategoryAndStatus("PC", "DONE")));

        tvMasterTotal.setText(String.valueOf(repo.countByCategory("MASTER")));
        tvMasterWork.setText(String.valueOf(repo.countByCategoryAndStatus("MASTER", "IN_PROGRESS")));
        tvMasterDone.setText(String.valueOf(repo.countByCategoryAndStatus("MASTER", "DONE")));
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshStats();
    }
}