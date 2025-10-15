package lab3.controller;

import lab3.model.Book;
import lab3.model.BookModel;
import lab3.view.BookView;
import java.util.List;

public class BookController {
    private BookModel model;
    private BookView view;

    public BookController(BookModel model, BookView view) {
        this.model = model;
        this.view = view;
    }

    public void run() {
        view.displayMessage("\nІніціалізація бібліотеки...\n");
        view.displayBooks(model.getAllBooks(), "ВИХІДНИЙ МАСИВ КНИГ");

        boolean running = true;
        while (running) {
            view.displayMenu();
            int choice = view.getMenuChoice();

            switch (choice) {
                case 1:
                    showAllBooks();
                    break;
                case 2:
                    findBooksByAuthor();
                    break;
                case 3:
                    findBooksByPublisher();
                    break;
                case 4:
                    findBooksAfterYear();
                    break;
                case 5:
                    sortByPublisher();
                    break;
                case 0:
                    running = false;
                    view.displayMessage("\nДякуємо за використання системи! До побачення!");
                    break;
                default:
                    view.displayError("Невірний вибір. Спробуйте ще раз.");
            }
        }
        view.close();
    }

    private void showAllBooks() {
        List<Book> books = model.getAllBooks();
        view.displayBooks(books, "\nВСІ КНИГИ В БІБЛІОТЕЦІ");
    }

    private void findBooksByAuthor() {
        String author = view.getAuthorInput();
        
        List<Book> books = model.findBooksByAuthor(author);
        view.displayBooks(books, "\nКНИГИ АВТОРА: " + author);
    }

    private void findBooksByPublisher() {
        String publisher = view.getPublisherInput();
        
        List<Book> books = model.findBooksByPublisher(publisher);
        view.displayBooks(books, "\nКНИГИ ВИДАВНИЦТВА: " + publisher);
    }

    private void findBooksAfterYear() {
        int year = view.getYearInput();
        
        if (year == -1) {
            view.displayError("Невірний формат року.");
            return;
        }
        
        List<Book> books = model.findBooksAfterYear(year);
        view.displayBooks(books, "\nКНИГИ, ВИДАНІ ПІСЛЯ " + year + " РОКУ");
    }

    private void sortByPublisher() {
        view.displayMessage("\nСортування книг за видавництвами...");
        List<Book> books = model.sortByPublisher();
        view.displayBooks(books, "\nКНИГИ, ВІДСОРТОВАНІ ЗА ВИДАВНИЦТВАМИ");
    }
}