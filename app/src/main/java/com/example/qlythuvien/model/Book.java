package com.example.qlythuvien.model;

import java.io.Serializable;

public class Book implements Serializable {
    private String title;
    private String author;
    private String category;
    private String status;      // "Chưa đọc", "Đang đọc", "Đã đọc"
    private float rating;       // 0 đến 5 sao
    private String note;

    public Book() {
    }

    public Book(String title, String author, String category, String status, float rating, String note) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.status = status;
        this.rating = rating;
        this.note = note;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
