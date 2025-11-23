package com.example.ganieva_k;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateRequestActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "id";

    private EditText etFirstName, etLastName, etMiddleName, etPhone, etModel, etEmail, etAddress, etComment;
    private Spinner spinnerCategory;
    private Button btnPickDate, btnPickTime, btnSubmit;
    private TextView tvPicked;
    private String pickedDate, pickedTime;
    private final Calendar calendar = Calendar.getInstance();
    private RequestRepo repo;
    private String editId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etMiddleName = findViewById(R.id.etMiddleName);
        etPhone = findViewById(R.id.etPhone);
        etModel = findViewById(R.id.etModel);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etComment = findViewById(R.id.etComment);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnPickTime = findViewById(R.id.btnPickTime);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvPicked = findViewById(R.id.tvPicked);

        repo = new RequestRepo(this);

        String[] categories = getResources().getStringArray(R.array.request_categories);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        SimpleDateFormat dfDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        pickedDate = dfDate.format(calendar.getTime());
        pickedTime = dfTime.format(calendar.getTime());
        tvPicked.setText("Дата: " + pickedDate + " Время: " + pickedTime);

        editId = getIntent().getStringExtra(EXTRA_ID);
        if (editId != null) {
            setTitle("Редактирование заявки");
            btnSubmit.setText("Сохранить");
            ContentValues v = repo.getById(editId);
            if (v != null) {
                etFirstName.setText(n(v.getAsString("first_name")));
                etLastName.setText(n(v.getAsString("last_name")));
                etMiddleName.setText(n(v.getAsString("middle_name")));
                etPhone.setText(n(v.getAsString("phone")));
                etModel.setText(n(v.getAsString("model")));
                etEmail.setText(n(v.getAsString("email")));
                etAddress.setText(n(v.getAsString("address")));
                etComment.setText(n(v.getAsString("comment")));

                String savedCategory = v.getAsString("category");
                String[] values = getResources().getStringArray(R.array.request_category_values);
                for (int i = 0; i < values.length; i++) {
                    if (values[i].equals(savedCategory)) {
                        spinnerCategory.setSelection(i);
                        break;
                    }
                }

                pickedDate = n(v.getAsString("created_date"), pickedDate);
                pickedTime = n(v.getAsString("created_time"), pickedTime);
                tvPicked.setText("Дата: " + pickedDate + " Время: " + pickedTime);
            }
        }

        btnPickDate.setOnClickListener(v -> {
            new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
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
                    (view, hourOfDay, minute) -> {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
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
            String phone = etPhone.getText().toString().trim();
            String model = etModel.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String comment = etComment.getText().toString().trim();

            String[] categoryValues = getResources().getStringArray(R.array.request_category_values);
            int selected = spinnerCategory.getSelectedItemPosition();
            String category = categoryValues[selected];
            boolean isMaster = "MASTER".equals(category);

            if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty() || comment.isEmpty()) {
                String msg = isMaster ? "Заполните все поля" : "Заполните все поля";
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                return;
            }

            if (editId == null) {
                repo.createRequest(firstName, lastName, middleName, phone, model, email, address, comment, category, pickedDate, pickedTime);
                Toast.makeText(this, "Заявка создана!", Toast.LENGTH_SHORT).show();
            } else {
                repo.updateRequest(editId, firstName, lastName, middleName, phone, model, email, address, comment, category);
                Toast.makeText(this, "Изменения сохранены", Toast.LENGTH_SHORT).show();
            }
            finish();
        });
    }

    private String n(String s) {
        return s == null ? "" : s;
    }

    private String n(String s, String def) {
        return (s == null || s.isEmpty()) ? def : s;
    }
}