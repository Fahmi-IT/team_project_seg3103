package src.main.java.main;

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
        this.borrower = null;
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
        borrower = null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return title.equals(book.title)
                && author.equals(book.author)
                && isbn.equals(book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, isbn);
    }
}

