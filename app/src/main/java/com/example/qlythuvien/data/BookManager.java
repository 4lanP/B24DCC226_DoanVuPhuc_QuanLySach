package com.example.qlythuvien.data;

import com.example.qlythuvien.model.Book;
import java.util.ArrayList;

public class BookManager {
    private static BookManager instance;
    private final ArrayList<Book> bookList;

    public static final String STATUS_NOT_READ = "Chưa đọc";
    public static final String STATUS_READING = "Đang đọc";
    public static final String STATUS_READ_DONE = "Đã đọc";

    private BookManager() {
        bookList = new ArrayList<>();
        initSampleData();
    }

    public static synchronized BookManager getInstance() {
        if (instance == null) {
            instance = new BookManager();
        }
        return instance;
    }

    private void initSampleData() {
        bookList.add(new Book(
                "Lập trình Android cơ bản",
                "Nguyễn Văn A",
                "Công nghệ",
                STATUS_READING,
                4.0f,
                "Sách dành cho người mới bắt đầu"
        ));
        bookList.add(new Book(
                "Đắc nhân tâm",
                "Dale Carnegie",
                "Kỹ năng sống",
                STATUS_READ_DONE,
                5.0f,
                "Sách kỹ năng sống rất hay"
        ));
        bookList.add(new Book(
                "Dế Mèn phiêu lưu ký",
                "Tô Hoài",
                "Văn học",
                STATUS_NOT_READ,
                0.0f,
                "Tác phẩm văn học thiếu nhi kinh điển"
        ));
    }

    public ArrayList<Book> getBooks() {
        return bookList;
    }

    public void addBook(Book book) {
        bookList.add(book);
    }

    public Book getBook(int position) {
        if (position >= 0 && position < bookList.size()) {
            return bookList.get(position);
        }
        return null;
    }

    public void updateBook(int position, Book updatedBook) {
        if (position >= 0 && position < bookList.size()) {
            bookList.set(position, updatedBook);
        }
    }

    public void deleteBook(int position) {
        if (position >= 0 && position < bookList.size()) {
            bookList.remove(position);
        }
    }

    public void markAsRead(int position) {
        if (position >= 0 && position < bookList.size()) {
            bookList.get(position).setStatus(STATUS_READ_DONE);
        }
    }

    public void updateRating(int position, float rating) {
        if (position >= 0 && position < bookList.size()) {
            bookList.get(position).setRating(rating);
        }
    }

    // Thống kê
    public int getTotalCount() {
        return bookList.size();
    }

    public int getReadCount() {
        int count = 0;
        for (Book b : bookList) {
            if (STATUS_READ_DONE.equalsIgnoreCase(b.getStatus())) {
                count++;
            }
        }
        return count;
    }

    public int getUnreadCount() {
        int count = 0;
        for (Book b : bookList) {
            if (STATUS_NOT_READ.equalsIgnoreCase(b.getStatus()) || STATUS_READING.equalsIgnoreCase(b.getStatus())) {
                count++;
            }
        }
        return count;
    }
}
