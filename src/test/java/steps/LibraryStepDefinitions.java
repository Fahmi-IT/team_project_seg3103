package steps;

import io.cucumber.java.en.*;
import main.Book;
import main.BookStore;
import main.Library;
import main.LibraryResult;
import main.MemberRegistry;
import io.cucumber.datatable.DataTable;

import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;

public class LibraryStepDefinitions {

    private Library library;
    private BookStore bookStore;
    private MemberRegistry memberRegistry;
    private LibraryResult lastResult;
    private List<Book> retrievedBooks;

    @Given("the library system is initialized")
    public void the_library_system_is_initialized() {
        bookStore = new InMemoryBookStore();
        memberRegistry = new InMemoryMemberRegistry();
        library = new Library(bookStore, memberRegistry);
    }

    @Given("the following members are registered:")
    public void the_following_members_are_registered(DataTable dataTable) {
        List<Map<String, String>> members = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> member : members) {
            memberRegistry.register(member.get("name"));
        }
    }

    @Given("the following books are available:")
    public void the_following_books_are_available(DataTable dataTable) {
        List<Map<String, String>> books = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> book : books) {
            Book newBook = new Book(book.get("title"), book.get("author"), book.get("isbn"));
            bookStore.add(newBook);
        }
    }

    @Given("a book with ISBN {string} already exists in the library")
    public void a_book_with_isbn_already_exists_in_the_library(String isbn) {
        Book existingBook = new Book("Existing Book", "Existing Author", isbn);
        bookStore.add(existingBook);
    }

    @Given("a member {string} is already registered")
    public void a_member_is_already_registered(String memberName) {
        memberRegistry.register(memberName);
    }

    @Given("{string} has borrowed the book with ISBN {string}")
    public void member_has_borrowed_the_book_with_isbn(String memberName, String isbn) {
        Book book = bookStore.findByIsbn(isbn);
        book.borrow(memberName);
    }

    @When("I add a book with title {string}, author {string}, and ISBN {string}")
    public void i_add_a_book_with_title_author_and_isbn(String title, String author, String isbn) {
        lastResult = library.addBook(title, author, isbn);
    }

    @When("I try to add a book with title {string}, author {string}, and ISBN {string}")
    public void i_try_to_add_a_book_with_title_author_and_isbn(String title, String author, String isbn) {
        lastResult = library.addBook(title, author, isbn);
    }

    @When("I try to add another book with the same ISBN {string}")
    public void i_try_to_add_another_book_with_the_same_isbn(String isbn) {
        lastResult = library.addBook("Another Book", "Another Author", isbn);
    }

    @When("I register a member with name {string}")
    public void i_register_a_member_with_name(String memberName) {
        lastResult = library.registerMember(memberName);
    }

    @When("I try to register a member with name {string}")
    public void i_try_to_register_a_member_with_name(String memberName) {
        lastResult = library.registerMember(memberName);
    }

    @When("{string} borrows the book with ISBN {string}")
    public void member_borrows_the_book_with_isbn(String memberName, String isbn) {
        lastResult = library.borrowBook(isbn, memberName);
    }

    @When("{string} tries to borrow a book with ISBN {string}")
    public void member_tries_to_borrow_a_book_with_isbn(String memberName, String isbn) {
        lastResult = library.borrowBook(isbn, memberName);
    }

    @When("{string} returns the book with ISBN {string}")
    public void member_returns_the_book_with_isbn(String memberName, String isbn) {
        lastResult = library.returnBook(isbn, memberName);
    }

    @When("{string} tries to return the book with ISBN {string}")
    public void member_tries_to_return_the_book_with_isbn(String memberName, String isbn) {
        lastResult = library.returnBook(isbn, memberName);
    }

    @When("I request the list of available books")
    public void i_request_the_list_of_available_books() {
        retrievedBooks = library.getAvailableBooks();
    }

    @When("I request the books borrowed by {string}")
    public void i_request_the_books_borrowed_by(String memberName) {
        retrievedBooks = library.getBorrowedBooksByMember(memberName);
    }

    @Then("the book should be added successfully")
    public void the_book_should_be_added_successfully() {
        Assertions.assertTrue(lastResult.isSuccess(), "Book addition should be successful");
    }

    @Then("the book addition should fail")
    public void the_book_addition_should_fail() {
        Assertions.assertFalse(lastResult.isSuccess(), "Book addition should fail");
    }

    @Then("the book addition should be {string}")
    public void the_book_addition_should_be(String expectedResult) {
        boolean expectedSuccess = "success".equals(expectedResult);
        Assertions.assertEquals(expectedSuccess, lastResult.isSuccess());
    }

    @Then("the book should be available in the library")
    public void the_book_should_be_available_in_the_library() {
        List<Book> availableBooks = library.getAvailableBooks();
        Assertions.assertTrue(availableBooks.size() > 0, "Library should have available books");
    }

    @Then("the member should be registered successfully")
    public void the_member_should_be_registered_successfully() {
        Assertions.assertTrue(lastResult.isSuccess(), "Member registration should be successful");
    }

    @Then("the member registration should fail")
    public void the_member_registration_should_fail() {
        Assertions.assertFalse(lastResult.isSuccess(), "Member registration should fail");
    }

    @Then("the member {string} should be in the system")
    public void the_member_should_be_in_the_system(String memberName) {
        Assertions.assertTrue(memberRegistry.isRegistered(memberName), 
            "Member should be registered in the system");
    }

    @Then("the book should be borrowed successfully")
    public void the_book_should_be_borrowed_successfully() {
        Assertions.assertTrue(lastResult.isSuccess(), "Book borrowing should be successful");
    }

    @Then("the borrowing should fail")
    public void the_borrowing_should_fail() {
        Assertions.assertFalse(lastResult.isSuccess(), "Book borrowing should fail");
    }

    @Then("the book {string} should be marked as borrowed by {string}")
    public void the_book_should_be_marked_as_borrowed_by(String bookTitle, String memberName) {
        List<Book> borrowedBooks = library.getBorrowedBooksByMember(memberName);
        boolean bookFound = borrowedBooks.stream()
            .anyMatch(book -> book.getTitle().equals(bookTitle) && book.getBorrower().equals(memberName));
        Assertions.assertTrue(bookFound, "Book should be borrowed by the member");
    }

    @Then("the book should be returned successfully")
    public void the_book_should_be_returned_successfully() {
        Assertions.assertTrue(lastResult.isSuccess(), "Book return should be successful");
    }

    @Then("the return should fail")
    public void the_return_should_fail() {
        Assertions.assertFalse(lastResult.isSuccess(), "Book return should fail");
    }

    @Then("the book {string} should be available for borrowing")
    public void the_book_should_be_available_for_borrowing(String bookTitle) {
        List<Book> availableBooks = library.getAvailableBooks();
        boolean bookFound = availableBooks.stream()
            .anyMatch(book -> book.getTitle().equals(bookTitle) && !book.isBorrowed());
        Assertions.assertTrue(bookFound, "Book should be available for borrowing");
    }

    @Then("I should see the error message {string}")
    public void i_should_see_the_error_message(String expectedMessage) {
        Assertions.assertEquals(expectedMessage, lastResult.getMessage());
    }

    @Then("I should see the message {string}")
    public void i_should_see_the_message(String expectedMessage) {
        Assertions.assertEquals(expectedMessage, lastResult.getMessage());
    }

    @Then("I should see {int} available book(s)")
    public void i_should_see_available_books(int expectedCount) {
        Assertions.assertEquals(expectedCount, retrievedBooks.size());
    }

    @Then("the available book should be {string} by {string}")
    public void the_available_book_should_be_by(String title, String author) {
        Assertions.assertEquals(1, retrievedBooks.size());
        Book book = retrievedBooks.get(0);
        Assertions.assertEquals(title, book.getTitle());
        Assertions.assertEquals(author, book.getAuthor());
    }

    @Then("I should see {int} borrowed book(s)")
    public void i_should_see_borrowed_books(int expectedCount) {
        Assertions.assertEquals(expectedCount, retrievedBooks.size());
    }

    @Then("the borrowed books should include {string} and {string}")
    public void the_borrowed_books_should_include_and(String title1, String title2) {
        List<String> titles = retrievedBooks.stream()
            .map(Book::getTitle)
            .toList();
        Assertions.assertTrue(titles.contains(title1) && titles.contains(title2),
            "Borrowed books should include both specified titles");
    }
}