package com.example.qlythuvien.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlythuvien.R;
import com.example.qlythuvien.data.BookManager;
import com.example.qlythuvien.model.Book;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    public interface OnBookClickListener {
        void onBookClick(Book book, int position);
    }

    private final Context context;
    private final ArrayList<Book> bookList;
    private final OnBookClickListener listener;

    public BookAdapter(Context context, ArrayList<Book> bookList, OnBookClickListener listener) {
        this.context = context;
        this.bookList = bookList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);

        holder.tvBookTitle.setText(book.getTitle());
        holder.tvBookAuthor.setText("Tác giả: " + book.getAuthor());
        holder.tvBookCategory.setText(book.getCategory());
        holder.rbBookRating.setRating(book.getRating());

        // Hiển thị tình trạng và định dạng màu sắc badge
        String status = book.getStatus();
        holder.tvBookStatus.setText(status);

        if (BookManager.STATUS_READ_DONE.equalsIgnoreCase(status)) {
            holder.tvBookStatus.setBackgroundResource(R.drawable.bg_badge_read);
            holder.tvBookStatus.setTextColor(ContextCompat.getColor(context, R.color.status_read));
        } else if (BookManager.STATUS_READING.equalsIgnoreCase(status)) {
            holder.tvBookStatus.setBackgroundResource(R.drawable.bg_badge_reading);
            holder.tvBookStatus.setTextColor(ContextCompat.getColor(context, R.color.status_reading));
        } else {
            holder.tvBookStatus.setBackgroundResource(R.drawable.bg_badge_unread);
            holder.tvBookStatus.setTextColor(ContextCompat.getColor(context, R.color.status_unread));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                int pos = holder.getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onBookClick(bookList.get(pos), pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList != null ? bookList.size() : 0;
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookTitle, tvBookAuthor, tvBookCategory, tvBookStatus;
        RatingBar rbBookRating;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
            tvBookCategory = itemView.findViewById(R.id.tvBookCategory);
            tvBookStatus = itemView.findViewById(R.id.tvBookStatus);
            rbBookRating = itemView.findViewById(R.id.rbBookRating);
        }
    }
}
