Feature: Library Management System
  As a librarian
  I want to manage books and members
  So that I can track book borrowing and returns

  Background:
    Given the library system is initialized
    And the following members are registered:
      | name        |
      | John Doe    |
      | Jane Smith  |
      | Bob Johnson |

  Scenario: Successfully add a new book to the library
    When I add a book with title "The Great Gatsby", author "F. Scott Fitzgerald", and ISBN "978-0-7432-7356-5"
    Then the book should be added successfully
    And the book should be available in the library

  Scenario: Cannot add a book with missing information
    When I try to add a book with title "", author "Unknown Author", and ISBN "123456789"
    Then the book addition should fail
    And I should see the error message "All fields (title, author, ISBN) are required"

  Scenario: Cannot add a book with duplicate ISBN
    Given a book with ISBN "978-0-7432-7356-5" already exists in the library
    When I try to add another book with the same ISBN "978-0-7432-7356-5"
    Then the book addition should fail
    And I should see the error message "Book with this ISBN already exists"

  Scenario: Successfully register a new member
    When I register a member with name "Alice Wonder"
    Then the member should be registered successfully
    And the member "Alice Wonder" should be in the system

  Scenario: Cannot register a member with empty name
    When I try to register a member with name ""
    Then the member registration should fail
    And I should see the error message "Member name cannot be empty"

  Scenario: Cannot register a member that already exists
    Given a member "John Doe" is already registered
    When I try to register a member with name "John Doe"
    Then the member registration should fail
    And I should see the error message "Member already registered"

  Scenario: Successfully borrow an available book
    Given the following books are available:
      | title           | author              | isbn              |
      | 1984           | George Orwell       | 978-0-452-28423-4 |
      | To Kill a Mockingbird | Harper Lee    | 978-0-06-112008-4 |
    When "John Doe" borrows the book with ISBN "978-0-452-28423-4"
    Then the book should be borrowed successfully
    And the book "1984" should be marked as borrowed by "John Doe"

  Scenario: Cannot borrow a book that doesn't exist
    When "John Doe" tries to borrow a book with ISBN "999-9-999-99999-9"
    Then the borrowing should fail
    And I should see the error message "Book not found in library"

  Scenario: Cannot borrow a book that is already borrowed
    Given the following books are available:
      | title | author        | isbn              |
      | Dune  | Frank Herbert | 978-0-441-17271-9 |
    And "Jane Smith" has borrowed the book with ISBN "978-0-441-17271-9"
    When "John Doe" tries to borrow a book with ISBN "978-0-441-17271-9"
    Then the borrowing should fail
    And I should see the error message "Book is already borrowed by Jane Smith"

  Scenario: Unregistered member cannot borrow a book
    Given the following books are available:
      | title     | author      | isbn              |
      | Moby Dick | Herman Melville | 978-0-14-243724-7 |
    When "Unknown Person" tries to borrow a book with ISBN "978-0-14-243724-7"
    Then the borrowing should fail
    And I should see the error message "Member not registered"

  Scenario: Successfully return a borrowed book
    Given the following books are available:
      | title        | author           | isbn              |
      | Animal Farm  | George Orwell    | 978-0-452-28424-1 |
    And "Bob Johnson" has borrowed the book with ISBN "978-0-452-28424-1"
    When "Bob Johnson" returns the book with ISBN "978-0-452-28424-1"
    Then the book should be returned successfully
    And the book "Animal Farm" should be available for borrowing

  Scenario: Cannot return a book that was not borrowed
    Given the following books are available:
      | title          | author          | isbn              |
      | Brave New World | Aldous Huxley  | 978-0-06-085052-4 |
    When "John Doe" tries to return the book with ISBN "978-0-06-085052-4"
    Then the return should fail
    And I should see the error message "Book is not currently borrowed"

  Scenario: Cannot return a book borrowed by someone else
    Given the following books are available:
      | title     | author      | isbn              |
      | Fahrenheit 451 | Ray Bradbury | 978-0-345-34296-8 |
    And "Jane Smith" has borrowed the book with ISBN "978-0-345-34296-8"
    When "John Doe" tries to return the book with ISBN "978-0-345-34296-8"
    Then the return should fail
    And I should see the error message "Book was not borrowed by this member"

  Scenario Outline: Add books with different validation scenarios
    When I try to add a book with title "<title>", author "<author>", and ISBN "<isbn>"
    Then the book addition should be "<result>"
    And I should see the message "<message>"

    Examples:
      | title          | author           | isbn              | result | message                                      |
      | Valid Book     | Valid Author     | 978-1-234-56789-0 | success | Book added successfully                      |
      |                | Valid Author     | 978-1-234-56789-1 | failure | All fields (title, author, ISBN) are required |
      | Valid Book     |                  | 978-1-234-56789-2 | failure | All fields (title, author, ISBN) are required |
      | Valid Book     | Valid Author     |                   | failure | All fields (title, author, ISBN) are required |

  Scenario: View available books
    Given the following books are available:
      | title           | author              | isbn              |
      | The Hobbit      | J.R.R. Tolkien     | 978-0-547-92822-7 |
      | Pride and Prejudice | Jane Austen     | 978-0-14-143951-8 |
    And "John Doe" has borrowed the book with ISBN "978-0-547-92822-7"
    When I request the list of available books
    Then I should see 1 available book
    And the available book should be "Pride and Prejudice" by "Jane Austen"

  Scenario: View books borrowed by a member
    Given the following books are available:
      | title              | author           | isbn              |
      | The Catcher in the Rye | J.D. Salinger | 978-0-316-76948-0 |
      | Lord of the Flies  | William Golding  | 978-0-571-05686-2 |
    And "Jane Smith" has borrowed the book with ISBN "978-0-316-76948-0"
    And "Jane Smith" has borrowed the book with ISBN "978-0-571-05686-2"
    When I request the books borrowed by "Jane Smith"
    Then I should see 2 borrowed books
    And the borrowed books should include "The Catcher in the Rye" and "Lord of the Flies"