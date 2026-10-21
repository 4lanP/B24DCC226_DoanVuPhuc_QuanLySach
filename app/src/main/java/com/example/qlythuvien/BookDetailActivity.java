package com.example.qlythuvien;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.qlythuvien.data.BookManager;
import com.example.qlythuvien.model.Book;
import com.google.android.material.button.MaterialButton;

public class BookDetailActivity extends AppCompatActivity {

    private ImageButton btnBackDetailHeader;
    private TextView tvDetailTitle;
    private TextView tvDetailAuthorCategory;
    private TextView tvDetailStatus;
    private RatingBar rbDetailRating;
    private TextView tvDetailRatingText;
    private TextView tvDetailNote;
    private MaterialButton btnMarkAsRead;
    private MaterialButton btnBack;
    private MaterialButton btnDeleteBook;

    private int bookPosition = -1;
    private Book currentBook;
    private BookManager bookManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        bookManager = BookManager.getInstance();
        bookPosition = getIntent().getIntExtra(MainActivity.EXTRA_BOOK_POSITION, -1);
        currentBook = bookManager.getBook(bookPosition);

        if (currentBook == null) {
            Toast.makeText(this, "Không tìm thấy thông tin cuốn sách!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        displayBookDetails();
        setupListeners();
    }

    private void initViews() {
        btnBackDetailHeader = findViewById(R.id.btnBackDetailHeader);
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailAuthorCategory = findViewById(R.id.tvDetailAuthorCategory);
        tvDetailStatus = findViewById(R.id.tvDetailStatus);
        rbDetailRating = findViewById(R.id.rbDetailRating);
        tvDetailRatingText = findViewById(R.id.tvDetailRatingText);
        tvDetailNote = findViewById(R.id.tvDetailNote);
        btnMarkAsRead = findViewById(R.id.btnMarkAsRead);
        btnBack = findViewById(R.id.btnBack);
        btnDeleteBook = findViewById(R.id.btnDeleteBook);
    }

    private void displayBookDetails() {
        tvDetailTitle.setText(currentBook.getTitle());
        tvDetailAuthorCategory.setText(currentBook.getAuthor() + " • " + currentBook.getCategory());

        updateStatusUI(currentBook.getStatus());

        rbDetailRating.setRating(currentBook.getRating());
        tvDetailRatingText.setText((int) currentBook.getRating() + "/5 sao");

        if (TextUtils.isEmpty(currentBook.getNote())) {
            tvDetailNote.setText("Không có ghi chú");
            tvDetailNote.setTextColor(ContextCompat.getColor(this, R.color.text_muted));
        } else {
            tvDetailNote.setText(currentBook.getNote());
            tvDetailNote.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        }
    }

    private void updateStatusUI(String status) {
        tvDetailStatus.setText(status);

        if (BookManager.STATUS_READ_DONE.equalsIgnoreCase(status)) {
            tvDetailStatus.setBackgroundResource(R.drawable.bg_badge_read);
            tvDetailStatus.setTextColor(ContextCompat.getColor(this, R.color.status_read));
            // Theo đề bài: Nếu sách đã ở trạng thái Đã đọc thì ẩn hoặc vô hiệu hóa nút
            btnMarkAsRead.setVisibility(View.GONE);
        } else {
            btnMarkAsRead.setVisibility(View.VISIBLE);
            if (BookManager.STATUS_READING.equalsIgnoreCase(status)) {
                tvDetailStatus.setBackgroundResource(R.drawable.bg_badge_reading);
                tvDetailStatus.setTextColor(ContextCompat.getColor(this, R.color.status_reading));
            } else {
                tvDetailStatus.setBackgroundResource(R.drawable.bg_badge_unread);
                tvDetailStatus.setTextColor(ContextCompat.getColor(this, R.color.status_unread));
            }
        }
    }

    private void setupListeners() {
        btnBackDetailHeader.setOnClickListener(v -> finish());
        btnBack.setOnClickListener(v -> finish());

        // Thay đổi đánh giá RatingBar trực tiếp
        rbDetailRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                currentBook.setRating(rating);
                bookManager.updateRating(bookPosition, rating);
                tvDetailRatingText.setText((int) rating + "/5 sao");
                Toast.makeText(this, "Đã cập nhật đánh giá: " + (int) rating + " sao", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút Đánh dấu đã đọc
        btnMarkAsRead.setOnClickListener(v -> {
            bookManager.markAsRead(bookPosition);
            updateStatusUI(BookManager.STATUS_READ_DONE);
            Toast.makeText(this, "Đã đánh dấu là Đã đọc", Toast.LENGTH_SHORT).show();
        });

        // Nút Xóa sách
        btnDeleteBook.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_delete_title)
                .setMessage(R.string.confirm_delete_msg)
                .setPositiveButton(R.string.btn_confirm_delete, (dialog, which) -> {
                    bookManager.deleteBook(bookPosition);
                    Toast.makeText(BookDetailActivity.this, "Đã xóa sách thành công", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton(R.string.btn_cancel, (dialog, which) -> dialog.dismiss())
                .show();
    }
}
