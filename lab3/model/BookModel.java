package lab3.model;

import java.util.*;
import java.util.stream.Collectors;

public class BookModel {
    private List<Book> books;
    private Random random;

    private static final String[] TITLES = {
        "Тіні забутих предків", "Захар Беркут", "Земля", "Кобзар", "Лісова пісня",
        "Місто", "Intermezzo", "Енеїда", "Камінний хрест", "Черлене вино",
        "Марія", "Мойсей", "Сад Гетсиманський", "Тигролови", "Украдене щастя"
    };

    private static final String[] AUTHORS = {
        "Михайло Коцюбинський", "Іван Франко", "Ольга Кобилянська",
        "Тарас Шевченко", "Леся Українка", "Валер'ян Підмогильний",
        "Михайло Стельмах", "Іван Котляревський", "Василь Стефаник",
        "Володимир Винниченко", "Уляна Кравченко", "Іван Франко"
    };

    private static final String[] PUBLISHERS = {
        "А-БА-БА-ГА-ЛА-МА-ГА", "Vivat", "Фоліо", "Ранок", "КСД",
        "Видавництво Старого Лева", "Клуб Сімейного Дозвілля", "Основи"
    };

    public BookModel() {
        books = new ArrayList<>();
        random = new Random();
        initializeBooks();
    }

    private void initializeBooks() {
        for (int i = 0; i < 12; i++) {
            books.add(new Book(
                TITLES[i % TITLES.length],
                AUTHORS[i % AUTHORS.length],
                PUBLISHERS[random.nextInt(PUBLISHERS.length)],
                1990 + random.nextInt(35),
                100 + random.nextInt(400),
                150 + random.nextDouble() * 350
            ));
        }
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    public List<Book> findBooksByAuthor(String author) {
        return books.stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .collect(Collectors.toList());
    }

    public List<Book> findBooksByPublisher(String publisher) {
        return books.stream()
                .filter(book -> book.getPublisher().equalsIgnoreCase(publisher))
                .collect(Collectors.toList());
    }

    public List<Book> findBooksAfterYear(int year) {
        return books.stream()
                .filter(book -> book.getYear() > year)
                .collect(Collectors.toList());
    }

    public List<Book> sortByPublisher() {
        List<Book> sorted = new ArrayList<>(books);
        sorted.sort(Comparator.comparing(Book::getPublisher)
                .thenComparing(Book::getTitle));
        return sorted;
    }
}
