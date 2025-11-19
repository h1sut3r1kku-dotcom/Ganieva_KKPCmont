package com.example.ganieva_k;

import android.content.ContentValues;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private EditText etFirstName, etLastName, etMiddleName, etEmail, etAddress;
    private Button btnEditProfile;
    private LinearLayout historyContainer;
    private boolean isEditing = false;
    private UserRepo userRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etFirstName = findViewById(R.id.textFirstName);
        etLastName = findViewById(R.id.textLastName);
        etMiddleName = findViewById(R.id.textMiddleName);
        etEmail = findViewById(R.id.textEmail);
        etAddress = findViewById(R.id.textAddress);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        historyContainer = findViewById(R.id.historyContainer);
        userRepo = new UserRepo(this);

        loadProfile();
        loadRequestHistory();

        btnEditProfile.setOnClickListener(v -> {
            if (isEditing) {
                saveProfile();
                setEditing(false);
            } else {
                setEditing(true);
            }
        });
    }

    private void loadProfile() {
        ContentValues profile = userRepo.getProfile();
        if (profile != null) {
            etFirstName.setText(profile.getAsString("first_name"));
            etLastName.setText(profile.getAsString("last_name"));
            etMiddleName.setText(n(profile.getAsString("middle_name")));
            etEmail.setText(profile.getAsString("email"));
            etAddress.setText(profile.getAsString("address"));
        } else {
            // Создаём демо-профиль
            userRepo.saveProfile("Иван", "Петров", "Иванович", "ivan@example.com", "г. Москва, ул. Пушкина, д. 10");
            loadProfile();
        }
    }

    private void saveProfile() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String middleName = etMiddleName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Заполните все обязательные поля", Toast.LENGTH_SHORT).show();
            return;
        }

        userRepo.saveProfile(firstName, lastName, middleName, email, address);
        Toast.makeText(this, "Профиль обновлён", Toast.LENGTH_SHORT).show();
        setEditing(false);
    }

    private void setEditing(boolean editing) {
        isEditing = editing;
        etFirstName.setEnabled(editing);
        etLastName.setEnabled(editing);
        etMiddleName.setEnabled(editing);
        etEmail.setEnabled(editing);
        etAddress.setEnabled(editing);
        btnEditProfile.setText(editing ? "Сохранить" : "Редактировать");
    }

    private void loadRequestHistory() {
        RequestRepo repo = new RequestRepo(this);
        List<ContentValues> requests = repo.getAllRequests();
        historyContainer.removeAllViews();

        if (requests.isEmpty()) {
            historyContainer.addView(createHistoryItem("История пуста", "", ""));
        } else {
            for (ContentValues r : requests) {
                String comment = n(r.getAsString("comment"));
                String date = n(r.getAsString("created_date"));
                String time = n(r.getAsString("created_time"));
                historyContainer.addView(createHistoryItem(comment, date, time));
            }
        }
    }

    private View createHistoryItem(String comment, String date, String time) {
        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setPadding(0, 8, 0, 8);
        item.setBackground(ContextCompat.getDrawable(this, R.drawable.input_bg));

        TextView tvComment = new TextView(this);
        tvComment.setText(comment.isEmpty() ? "Без комментария" : comment);
        tvComment.setTextColor(Color.WHITE);
        tvComment.setTextSize(14);

        TextView tvDateTime = new TextView(this);
        tvDateTime.setText(date + " " + time);
        tvDateTime.setTextColor(Color.GRAY);
        tvDateTime.setTextSize(12);

        item.addView(tvComment);
        item.addView(tvDateTime);
        return item;
    }

    private String n(String s) {
        return s == null ? "" : s;
    }
}