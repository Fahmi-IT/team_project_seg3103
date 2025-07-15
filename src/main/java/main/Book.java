package main;

import java.util.Objects;

/**
 * main.Library Management System - Main Classes
 * Compatible with IntelliJ IDEA and JUnit 5 for code coverage analysis
 */

public class Book {
    private String title;
    private String author;
    private String isbn;
    private boolean isBorrowed;
    private String borrower;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isBorrowed = false;
        this.borrower = "";
    }

    // Getters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public boolean isBorrowed() { return isBorrowed; }
    public String getBorrower() { return borrower; }

    // Setters
    public void setBorrowed(boolean borrowed) { this.isBorrowed = borrowed; }
    public void setBorrower(String borrower) { this.borrower = borrower; }

    public void borrow(String memberName) {
        isBorrowed = true;
        borrower = memberName;
    }

    public void returnBook() {
        isBorrowed = false;
        borrower = "";
    }

    @Override
    public boolean equals(Object o) {
        try {
            Book b = (Book)o;
            if (o == null) {
                return false;
            } else if (this == o) {
                return true;
            } else if (getClass() != o.getClass()) {
                return false;
            } else if (b.title == title || b.author == author) {
                if (b.isbn.equals(isbn)) {
                    return true;
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, isbn);
    }
}

