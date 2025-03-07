import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

// Book class to store details
class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private String author;
    private String isbn;
    private boolean isBorrowed;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isBorrowed = false;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getISBN() {
        return isbn;
    }

    public boolean isAvailable() {
        return !isBorrowed;
    }

    public void borrowBook() {
        if (!isBorrowed) {
            isBorrowed = true;
            System.out.println("You borrowed: " + title);
        } else {
            System.out.println("Book is already borrowed.");
        }
    }

    public void returnBook() {
        if (isBorrowed) {
            isBorrowed = false;
            System.out.println("You returned: " + title);
        } else {
            System.out.println("This book was not borrowed.");
        }
    }

    @Override
    public String toString() {
        return title + " by " + author + " (ISBN: " + isbn + ") - " + (isBorrowed ? "Borrowed" : "Available");
    }
}

// Library class to manage books
class Library {
    private ArrayList<Book> books;
    private static final String FILE_NAME = "library_data.ser";

    public Library() {
        books = new ArrayList<>();
        loadBooks();                                              // Load books from file at startup
    }

    public void addBook(String title, String author, String isbn) {
        books.add(new Book(title, author, isbn));
        saveBooks();
        System.out.println("Book added: " + title);
    }

    public void displayBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }
        System.out.println("\nLibrary Books:");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    public void borrowBook(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title) && book.isAvailable()) {
                book.borrowBook();
                saveBooks();
                return;
            }
        }
        System.out.println("Book not found or already borrowed.");
    }

    public void returnBook(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                book.returnBook();
                saveBooks();
                return;
            }
        }
        System.out.println("Book not found in library.");
    }

    public void searchBook(String keyword) {
        boolean found = false;
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                book.getAuthor().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(book);
                found = true;
            }
        }
        if (!found) {
            System.out.println(" No books found matching: " + keyword);
        }
    }

    public void deleteBook(String isbn) {
        books.removeIf(book -> book.getISBN().equalsIgnoreCase(isbn));
        saveBooks();
        System.out.println("Book deleted successfully.");
    }

    // Save books to a file
    private void saveBooks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(books);
        } catch (IOException e) {
            System.out.println("Error saving books.");
        }
    }

    // Load books from a file
    private void loadBooks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            books = (ArrayList<Book>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            books = new ArrayList<>();
        }
    }
}

// Main class
public class LibraryManagementSystem {
    public static void main(String[] args) {
        Library library = new Library();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== 📚 Library Management System ===");
            System.out.println("1️.Add Book");
            System.out.println("2️.Display Books");
            System.out.println("3️.Borrow Book");
            System.out.println("4️.Return Book");
            System.out.println("5️.Search Book");
            System.out.println("6️.Delete Book");
            System.out.println("7️.Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter book title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter ISBN: ");
                    String isbn = scanner.nextLine();
                    library.addBook(title, author, isbn);
                    break;
                case 2:
                    library.displayBooks();
                    break;
                case 3:
                    System.out.print("Enter book title to borrow: ");
                    String borrowTitle = scanner.nextLine();
                    library.borrowBook(borrowTitle);
                    break;
                case 4:
                    System.out.print("Enter book title to return: ");
                    String returnTitle = scanner.nextLine();
                    library.returnBook(returnTitle);
                    break;
                case 5:
                    System.out.print(" Enter book title or author to search: ");
                    String keyword = scanner.nextLine();
                    library.searchBook(keyword);
                    break;
                case 6:
                    System.out.print("Enter ISBN of book to delete: ");
                    String deleteISBN = scanner.nextLine();
                    library.deleteBook(deleteISBN);
                    break;
                case 7:
                    System.out.println(" Exiting Library System. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice !!, Please try again.");
            }
        }
    }
}
