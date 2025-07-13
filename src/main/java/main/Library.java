package src.main.java.main;

import java.util.ArrayList;
import java.util.List;

public class Library {

    private final BookStore bookStore;
    private final MemberRegistry memberRegistry;

    public Library(BookStore bookStore, MemberRegistry memberRegistry) {
        this.bookStore = bookStore;
        this.memberRegistry = memberRegistry;
    }

    public LibraryResult addBook(String title, String author, String isbn) {
        if (isNullOrBlank(title) || isNullOrBlank(author) || isNullOrBlank(isbn)) {
            return new LibraryResult(false, "All fields (title, author, ISBN) are required");
        }

        if (bookStore.findByIsbn(isbn) != null) {
            return new LibraryResult(false, "Book with this ISBN already exists");
        }

        Book book = new Book(title, author, isbn);
        boolean added = bookStore.add(book);

        return added
                ? new LibraryResult(true, "Book added successfully")
                : new LibraryResult(false, "Failed to add book");
    }

    public LibraryResult registerMember(String name) {
        if (isNullOrBlank(name)) {
            return new LibraryResult(false, "Member name cannot be empty");
        }

        boolean registered = memberRegistry.register(name);
        if (!registered) {
            return new LibraryResult(false, "Member already registered");
        }

        return new LibraryResult(true, "Member registered successfully");
    }

    public LibraryResult borrowBook(String isbn, String memberName) {
        if (!memberRegistry.isRegistered(memberName)) {
            return new LibraryResult(false, "Member not registered");
        }

        Book book = bookStore.findByIsbn(isbn);
        if (book == null) {
            return new LibraryResult(false, "Book not found in library");
        }

        if (book.isBorrowed()) {
            return new LibraryResult(false, "Book is already borrowed by " + book.getBorrower());
        }

        book.borrow(memberName);
        return new LibraryResult(true, "Book '" + book.getTitle() + "' borrowed successfully");
    }

    public LibraryResult returnBook(String isbn, String memberName) {
        Book book = bookStore.findByIsbn(isbn);
        if (book == null) {
            return new LibraryResult(false, "Book not found in library");
        }

        if (!book.isBorrowed()) {
            return new LibraryResult(false, "Book is not currently borrowed");
        }

        if (!memberName.equals(book.getBorrower())) {
            return new LibraryResult(false, "Book was not borrowed by this member");
        }

        book.returnBook();
        return new LibraryResult(true, "Book '" + book.getTitle() + "' returned successfully");
    }

    public List<Book> getAvailableBooks() {
        return bookStore.getAvailableBooks();
    }

    public List<Book> getBorrowedBooksByMember(String memberName) {
        if (!memberRegistry.isRegistered(memberName)) {
            return new ArrayList<>();
        }
        return bookStore.getBorrowedBooksBy(memberName);
    }

    private boolean isNullOrBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}