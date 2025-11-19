package com.example.ganieva_k;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateRequestActivity extends AppCompatActivity {
    private EditText etFirstName, etLastName, etMiddleName, etEmail, etAddress, etComment;
    private Button btnPickDate, btnPickTime, btnSubmit;
    private TextView tvPicked;
    private String pickedDate, pickedTime;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etMiddleName = findViewById(R.id.etMiddleName);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etComment = findViewById(R.id.etComment);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnPickTime = findViewById(R.id.btnPickTime);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvPicked = findViewById(R.id.tvPicked);

        SimpleDateFormat dfDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        pickedDate = dfDate.format(calendar.getTime());
        pickedTime = dfTime.format(calendar.getTime());
        tvPicked.setText("Дата: " + pickedDate + " Время: " + pickedTime);

        // Загружаем данные профиля
        UserRepo userRepo = new UserRepo(this);
        ContentValues profile = userRepo.getProfile();
        if (profile != null) {
            etFirstName.setText(profile.getAsString("first_name"));
            etLastName.setText(profile.getAsString("last_name"));
            etMiddleName.setText(n(profile.getAsString("middle_name")));
            etEmail.setText(profile.getAsString("email"));
            etAddress.setText(profile.getAsString("address"));
        }

        btnPickDate.setOnClickListener(v -> {
            new DatePickerDialog(this,
                    (view, year, month, day) -> {
                        calendar.set(year, month, day);
                        pickedDate = dfDate.format(calendar.getTime());
                        tvPicked.setText("Дата: " + pickedDate + " Время: " + pickedTime);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        btnPickTime.setOnClickListener(v -> {
            new TimePickerDialog(this,
                    (view, hour, minute) -> {
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        pickedTime = dfTime.format(calendar.getTime());
                        tvPicked.setText("Дата: " + pickedDate + " Время: " + pickedTime);
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
            ).show();
        });

        btnSubmit.setOnClickListener(v -> {
            String firstName = etFirstName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String middleName = etMiddleName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String comment = etComment.getText().toString().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || address.isEmpty() || comment.isEmpty()) {
                Toast.makeText(this, "Заполните все обязательные поля", Toast.LENGTH_SHORT).show();
                return;
            }

            RequestRepo repo = new RequestRepo(this);
            repo.createRequest(firstName, lastName, middleName, email, address, comment, pickedDate, pickedTime);
            Toast.makeText(this, "Заявка создана!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private String n(String s) {
        return s == null ? "" : s;
    }
}