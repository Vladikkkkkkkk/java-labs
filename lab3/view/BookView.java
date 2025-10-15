package lab3.view;

import lab3.model.Book;
import java.util.List;
import java.util.Scanner;

public class BookView {
    private Scanner scanner;

    public BookView() {
        scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        System.out.println("\nСИСТЕМА УПРАВЛІННЯ БІБЛІОТЕКОЮ");
        System.out.println("1. Показати всі книги");
        System.out.println("2. Знайти книги за автором");
        System.out.println("3. Знайти книги за видавництвом");
        System.out.println("4. Знайти книги, видані пізніше вказаного року");
        System.out.println("5. Відсортувати книги за видавництвами");
        System.out.println("0. Вихід");
        System.out.print("Оберіть опцію: ");
    }

    public int getMenuChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public String getAuthorInput() {
        System.out.print("Введіть ім'я автора: ");
        return scanner.nextLine();
    }

    public String getPublisherInput() {
        System.out.print("Введіть назву видавництва: ");
        return scanner.nextLine();
    }

    public int getYearInput() {
        System.out.print("Введіть рік: ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void displayBooks(List<Book> books, String header) {
        System.out.println("\n" + header);
        
        if (books.isEmpty()) {
            System.out.println("Книги не знайдено.");
        } else {
            System.out.printf("%-30s %-20s %-20s %-4s %-10s %-10s%n",
                    "Назва", "Автор", "Видавництво", "Рік", "Сторінки", "Ціна");
            
            for (Book book : books) {
                System.out.println(book);
            }
            System.out.println("\nВсього знайдено: " + books.size() + " книг(и)");
        }
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayError(String error) {
        System.out.println("Помилка: " + error);
    }

    public void close() {
        scanner.close();
    }
}