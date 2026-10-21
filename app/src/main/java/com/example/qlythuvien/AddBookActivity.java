package com.example.qlythuvien;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlythuvien.data.BookManager;
import com.example.qlythuvien.model.Book;
import com.google.android.material.button.MaterialButton;

public class AddBookActivity extends AppCompatActivity {

    private ImageButton btnBackAdd;
    private EditText edtBookTitle;
    private EditText edtBookAuthor;
    private Spinner spCategory;
    private RadioGroup rgStatus;
    private RadioButton rbNotRead;
    private RadioButton rbReading;
    private RadioButton rbReadDone;
    private RatingBar rbAddRating;
    private TextView tvRatingValue;
    private EditText edtBookNote;
    private MaterialButton btnSaveBook;

    private static final String[] CATEGORIES = {
            "Văn học",
            "Khoa học",
            "Công nghệ",
            "Kỹ năng sống",
            "Khác"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        initViews();
        setupSpinner();
        setupRatingBar();
        setupListeners();
    }

    private void initViews() {
        btnBackAdd = findViewById(R.id.btnBackAdd);
        edtBookTitle = findViewById(R.id.edtBookTitle);
        edtBookAuthor = findViewById(R.id.edtBookAuthor);
        spCategory = findViewById(R.id.spCategory);
        rgStatus = findViewById(R.id.rgStatus);
        rbNotRead = findViewById(R.id.rbNotRead);
        rbReading = findViewById(R.id.rbReading);
        rbReadDone = findViewById(R.id.rbReadDone);
        rbAddRating = findViewById(R.id.rbAddRating);
        tvRatingValue = findViewById(R.id.tvRatingValue);
        edtBookNote = findViewById(R.id.edtBookNote);
        btnSaveBook = findViewById(R.id.btnSaveBook);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                CATEGORIES
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);
    }

    private void setupRatingBar() {
        rbAddRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            tvRatingValue.setText((int) rating + "/5 sao");
        });
    }

    private void setupListeners() {
        btnBackAdd.setOnClickListener(v -> finish());

        btnSaveBook.setOnClickListener(v -> handleSaveBook());
    }

    private void handleSaveBook() {
        String title = edtBookTitle.getText().toString().trim();
        String author = edtBookAuthor.getText().toString().trim();
        String category = spCategory.getSelectedItem() != null ? spCategory.getSelectedItem().toString() : "Khác";
        String note = edtBookNote.getText().toString().trim();
        float rating = rbAddRating.getRating();

        // 1. Kiểm tra validation không được để trống tên sách và tác giả
        if (TextUtils.isEmpty(title)) {
            edtBookTitle.setError(getString(R.string.err_empty_book_name));
            edtBookTitle.requestFocus();
            Toast.makeText(this, R.string.err_empty_book_name, Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(author)) {
            edtBookAuthor.setError(getString(R.string.err_empty_author));
            edtBookAuthor.requestFocus();
            Toast.makeText(this, R.string.err_empty_author, Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Xác định tình trạng đọc
        String status = BookManager.STATUS_NOT_READ;
        int checkedId = rgStatus.getCheckedRadioButtonId();
        if (checkedId == R.id.rbReading) {
            status = BookManager.STATUS_READING;
        } else if (checkedId == R.id.rbReadDone) {
            status = BookManager.STATUS_READ_DONE;
        }

        // 3. Tạo đối tượng Book mới và thêm vào BookManager
        Book newBook = new Book(title, author, category, status, rating, note);
        BookManager.getInstance().addBook(newBook);

        Toast.makeText(this, "Đã thêm cuốn sách \"" + title + "\" thành công!", Toast.LENGTH_SHORT).show();

        // 4. Trả về màn hình danh sách
        finish();
    }
}
