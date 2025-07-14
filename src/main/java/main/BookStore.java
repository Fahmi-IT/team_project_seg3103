package main;

import java.util.*;

public interface BookStore {
    boolean add(Book book);
    boolean remove(String isbn);
    Book findByIsbn(String isbn);
    List<Book> getAvailableBooks();
    List<Book> getBorrowedBooksBy(String memberName);
}