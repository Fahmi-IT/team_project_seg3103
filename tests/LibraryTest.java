package tests;

import main.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.easymock.EasyMock;

public class LibraryTest {

    private BookStore mockBookStore;
    private MemberRegistry mockMemberRegistry;
    private Library library;

    @BeforeEach
    void setUp() {
        mockBookStore = EasyMock.createMock(BookStore.class);
        mockMemberRegistry = EasyMock.createMock(MemberRegistry.class);
        library = new Library(mockBookStore, mockMemberRegistry);
    }

    @Test
    @DisplayName("Add Book - Valid Book Should Succeed")
    void testAddBookSuccess() {
        Book book = new Book("1984", "George Orwell", "123");

        EasyMock.expect(mockBookStore.findByIsbn("123")).andReturn(null);
        EasyMock.expect(mockBookStore.add(EasyMock.eq(book))).andReturn(true);
        EasyMock.replay(mockBookStore);

        LibraryResult result = library.addBook("1984", "George Orwell", "123");
        assertTrue(result.isSuccess());
        assertEquals("Book added successfully", result.getMessage());

        EasyMock.verify(mockBookStore);
    }

    @Test
    @DisplayName("Register Member - Duplicate Should Fail")
    void testDuplicateMemberRegistration() {
        EasyMock.expect(mockMemberRegistry.register("Alice")).andReturn(false);
        EasyMock.replay(mockMemberRegistry);

        LibraryResult result = library.registerMember("Alice");
        assertFalse(result.isSuccess());
        assertEquals("Member already registered", result.getMessage());

        EasyMock.verify(mockMemberRegistry);
    }

    @Test
    @DisplayName("Borrow Book - Should fail if book is already borrowed")
    void testBorrowBook_AlreadyBorrowed() {
        String isbn = "123-XYZ";
        String memberName = "Alice";

        Book borrowedBook = new Book("1984", "George Orwell", isbn);
        borrowedBook.setBorrowed(true);  // Assuming such a method exists

        // Mock interactions
        EasyMock.expect(mockBookStore.findByIsbn(isbn)).andReturn(borrowedBook).atLeastOnce();
        EasyMock.expect(mockMemberRegistry.isRegistered(memberName)).andReturn(true).anyTimes();
        EasyMock.replay(mockBookStore, mockMemberRegistry);

        // Act
        LibraryResult result = library.borrowBook(isbn, memberName);

        // Assert
        assertFalse(result.isSuccess());
        assertEquals("Book is already borrowed by null", result.getMessage()); //borrower is not named, defaults to null

        EasyMock.verify(mockBookStore, mockMemberRegistry);
    }
}