package com.example.ganieva_k;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.List;
import android.content.ContentValues;

public class RequestListActivity extends AppCompatActivity {

    private LinearLayout containerTech, containerPC, containerMaster;
    private Button
            btnSortTechDate, btnSortTechName, btnSortTechStatus,
            btnSortPcDate, btnSortPcName, btnSortPcStatus,
            btnSortMasterDate, btnSortMasterName, btnSortMasterStatus;
    private RequestRepo repo;

    // Порядок сортировки для каждой категории
    private String sortTech = "created_date DESC, created_time DESC";
    private String sortPC = "created_date DESC, created_time DESC";
    private String sortMaster = "created_date DESC, created_time DESC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        containerTech = findViewById(R.id.container_tech);
        containerPC = findViewById(R.id.container_pc);
        containerMaster = findViewById(R.id.container_master);

        // Кнопки сортировки
        btnSortTechDate = findViewById(R.id.btnSortTechDate);
        btnSortTechName = findViewById(R.id.btnSortTechName);
        btnSortTechStatus = findViewById(R.id.btnSortTechStatus);

        btnSortPcDate = findViewById(R.id.btnSortPcDate);
        btnSortPcName = findViewById(R.id.btnSortPcName);
        btnSortPcStatus = findViewById(R.id.btnSortPcStatus);

        btnSortMasterDate = findViewById(R.id.btnSortMasterDate);
        btnSortMasterName = findViewById(R.id.btnSortMasterName);
        btnSortMasterStatus = findViewById(R.id.btnSortMasterStatus);

        repo = new RequestRepo(this);

        setupSortListeners();
        loadRequests();
    }

    private void setupSortListeners() {
        // Ремонт техники
        btnSortTechDate.setOnClickListener(v -> {
            sortTech = "created_date DESC, created_time DESC";
            loadRequests();
        });
        btnSortTechName.setOnClickListener(v -> {
            sortTech = "first_name ASC, last_name ASC";
            loadRequests();
        });
        btnSortTechStatus.setOnClickListener(v -> {
            sortTech = "status ASC, created_date DESC, created_time DESC";
            loadRequests();
        });

        // Ремонт ПК
        btnSortPcDate.setOnClickListener(v -> {
            sortPC = "created_date DESC, created_time DESC";
            loadRequests();
        });
        btnSortPcName.setOnClickListener(v -> {
            sortPC = "first_name ASC, last_name ASC";
            loadRequests();
        });
        btnSortPcStatus.setOnClickListener(v -> {
            sortPC = "status ASC, created_date DESC, created_time DESC";
            loadRequests();
        });

        // Вызов мастера
        btnSortMasterDate.setOnClickListener(v -> {
            sortMaster = "created_date DESC, created_time DESC";
            loadRequests();
        });
        btnSortMasterName.setOnClickListener(v -> {
            sortMaster = "first_name ASC, last_name ASC";
            loadRequests();
        });
        btnSortMasterStatus.setOnClickListener(v -> {
            sortMaster = "status ASC, created_date DESC, created_time DESC";
            loadRequests();
        });
    }

    private void loadRequests() {
        loadCategory("TECH", containerTech, sortTech);
        loadCategory("PC", containerPC, sortPC);
        loadCategory("MASTER", containerMaster, sortMaster);
    }

    private void loadCategory(String category, LinearLayout container, String orderBy) {
        container.removeAllViews();
        List<ContentValues> requests = repo.listByCategory(category, orderBy);

        if (requests.isEmpty()) {
            container.addView(createEmptyMessage("Нет заявок"));
        } else {
            for (ContentValues r : requests) {
                String firstName = n(r.getAsString("first_name"));
                String lastName = n(r.getAsString("last_name"));
                String owner = (firstName + " " + lastName).trim();
                if (owner.isEmpty()) owner = "— Без имени —";

                String model = n(r.getAsString("model"));
                String date = n(r.getAsString("created_date"));
                String time = n(r.getAsString("created_time"));
                String status = "DONE".equals(r.getAsString("status")) ? "Выполнено" : "В работе";
                String id = n(r.getAsString("id"));

                container.addView(createRequestItem(owner, model, date, time, status, id));
            }
        }
    }

    private View createEmptyMessage(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextColor(Color.GRAY);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(0, 16, 0, 16);
        return tv;
    }

    private View createRequestItem(String owner, String model, String date, String time, String status, String id) {
        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setPadding(0, 12, 0, 12);

        TextView tvOwner = new TextView(this);
        tvOwner.setText(owner);
        tvOwner.setTextColor(Color.WHITE);
        tvOwner.setTextSize(16);
        tvOwner.setPadding(0, 0, 0, 4);

        TextView tvModel = new TextView(this);
        tvModel.setText(model.isEmpty() ? "— Без модели —" : model);
        tvModel.setTextColor(Color.WHITE);
        tvModel.setTextSize(14);
        tvModel.setPadding(0, 0, 0, 4);

        TextView tvDateTime = new TextView(this);
        tvDateTime.setText(date + " " + time);
        tvDateTime.setTextColor(Color.GRAY);
        tvDateTime.setTextSize(12);
        tvDateTime.setPadding(0, 0, 0, 8);

        TextView tvStatus = new TextView(this);
        tvStatus.setText(status);
        tvStatus.setTextColor("Выполнено".equals(status) ? Color.GREEN : Color.YELLOW);
        tvStatus.setTextSize(12);

        LinearLayout btnLayout = new LinearLayout(this);
        btnLayout.setOrientation(LinearLayout.HORIZONTAL);
        btnLayout.setGravity(Gravity.END);

        Button btnEdit = new Button(this);
        btnEdit.setText("✎");
        btnEdit.setTextSize(12);
        btnEdit.setPadding(8, 4, 8, 4);
        btnEdit.setOnClickListener(v -> {
            Intent i = new Intent(this, CreateRequestActivity.class);
            i.putExtra(CreateRequestActivity.EXTRA_ID, id);
            startActivity(i);
        });

        Button btnDone = new Button(this);
        btnDone.setText("✓");
        btnDone.setTextSize(12);
        btnDone.setPadding(8, 4, 8, 4);
        btnDone.setOnClickListener(v -> {
            repo.markDone(id, today(), now());
            Toast.makeText(this, "Заявка завершена", Toast.LENGTH_SHORT).show();
            loadRequests();
        });

        Button btnDelete = new Button(this);
        btnDelete.setText("✗");
        btnDelete.setTextSize(12);
        btnDelete.setPadding(8, 4, 8, 4);
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Удалить заявку?")
                    .setMessage("Это действие нельзя отменить.")
                    .setPositiveButton("Да", (d, w) -> {
                        repo.delete(id);
                        Toast.makeText(this, "Заявка удалена", Toast.LENGTH_SHORT).show();
                        loadRequests();
                    })
                    .setNegativeButton("Нет", null)
                    .show();
        });

        btnLayout.addView(btnEdit);
        btnLayout.addView(btnDone);
        btnLayout.addView(btnDelete);

        item.addView(tvOwner);
        item.addView(tvModel);
        item.addView(tvDateTime);
        item.addView(tvStatus);
        item.addView(btnLayout);

        return item;
    }

    private String today() {
        return new java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault()).format(new java.util.Date());
    }

    private String now() {
        return new java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(new java.util.Date());
    }

    private String n(String s) {
        return s == null ? "" : s;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRequests();
    }
}