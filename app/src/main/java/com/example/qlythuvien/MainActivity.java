package com.example.qlythuvien;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlythuvien.adapter.BookAdapter;
import com.example.qlythuvien.data.BookManager;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_BOOK_POSITION = "extra_book_position";

    private TextView tvTotalBooks;
    private TextView tvReadBooks;
    private TextView tvUnreadBooks;
    private RecyclerView rvBooks;
    private TextView tvEmptyState;
    private MaterialButton btnAddBook;

    private BookAdapter bookAdapter;
    private BookManager bookManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookManager = BookManager.getInstance();

        initViews();
        setupRecyclerView();
        setupListeners();
        updateStatistics();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật lại danh sách và thống kê khi quay lại từ Add hoặc Detail
        if (bookAdapter != null) {
            bookAdapter.notifyDataSetChanged();
        }
        updateStatistics();
        checkEmptyState();
    }

    private void initViews() {
        tvTotalBooks = findViewById(R.id.tvTotalBooks);
        tvReadBooks = findViewById(R.id.tvReadBooks);
        tvUnreadBooks = findViewById(R.id.tvUnreadBooks);
        rvBooks = findViewById(R.id.rvBooks);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        btnAddBook = findViewById(R.id.btnAddBook);
    }

    private void setupRecyclerView() {
        rvBooks.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(this, bookManager.getBooks(), (book, position) -> {
            // Mở màn hình chi tiết sách khi nhấn vào item
            Intent intent = new Intent(MainActivity.this, BookDetailActivity.class);
            intent.putExtra(EXTRA_BOOK_POSITION, position);
            startActivity(intent);
        });
        rvBooks.setAdapter(bookAdapter);
    }

    private void setupListeners() {
        // Nút Thêm sách mở AddBookActivity
        btnAddBook.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
            startActivity(intent);
        });
    }

    // Cập nhật khối thống kê
    private void updateStatistics() {
        tvTotalBooks.setText(String.valueOf(bookManager.getTotalCount()));
        tvReadBooks.setText(String.valueOf(bookManager.getReadCount()));
        tvUnreadBooks.setText(String.valueOf(bookManager.getUnreadCount()));
    }

    private void checkEmptyState() {
        if (bookManager.getTotalCount() == 0) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvBooks.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvBooks.setVisibility(View.VISIBLE);
        }
    }
}
