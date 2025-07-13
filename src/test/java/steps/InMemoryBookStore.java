package src.test.java.steps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.main.java.main.Book;
import src.main.java.main.BookStore;
import src.main.java.main.MemberRegistry;

/**
 * In-memory implementation of BookStore for testing
 */
class InMemoryBookStore implements BookStore {
    private final Map<String, Book> books = new HashMap<>();

    @Override
    public boolean add(Book book) {
        if (books.containsKey(book.getIsbn())) {
            return false;
        }
        books.put(book.getIsbn(), book);
        return true;
    }

    @Override
    public boolean remove(String isbn) {
        return books.remove(isbn) != null;
    }

    @Override
    public Book findByIsbn(String isbn) {
        return books.get(isbn);
    }

    @Override
    public List<Book> getAvailableBooks() {
        return books.values().stream()
            .filter(book -> !book.isBorrowed())
            .toList();
    }

    @Override
    public List<Book> getBorrowedBooksBy(String memberName) {
        return books.values().stream()
            .filter(book -> book.isBorrowed() && memberName.equals(book.getBorrower()))
            .toList();
    }
}

/**
 * In-memory implementation of MemberRegistry for testing
 */
class InMemoryMemberRegistry implements MemberRegistry {
    private final List<String> members = new ArrayList<>();

    @Override
    public boolean register(String name) {
        if (members.contains(name)) {
            return false;
        }
        members.add(name);
        return true;
    }

    @Override
    public boolean isRegistered(String name) {
        return members.contains(name);
    }
}