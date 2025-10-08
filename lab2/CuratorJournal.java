package lab2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CuratorJournal {
    static class JournalEntry {
        private String lastName;
        private String firstName;
        private LocalDate dateOfBirth;
        private String phoneNumber;
        private Address address;

        public JournalEntry(String lastName, String firstName, LocalDate dateOfBirth, 
                          String phoneNumber, Address address) {
            this.lastName = lastName;
            this.firstName = firstName;
            this.dateOfBirth = dateOfBirth;
            this.phoneNumber = phoneNumber;
            this.address = address;
        }

        @Override
        public String toString() {
            return String.format("Student: %s %s\nDate of Birth: %s\nPhone: %s\nAddress: %s",
                    lastName, firstName, 
                    dateOfBirth.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), 
                    phoneNumber, address);
        }
    }

    static class Address {
        private String street;
        private String houseNumber;
        private String apartment;

        public Address(String street, String houseNumber, String apartment) {
            this.street = street;
            this.houseNumber = houseNumber;
            this.apartment = apartment;
        }

        @Override
        public String toString() {
            return String.format("%s, house %s, apt %s", street, houseNumber, apartment);
        }
    }

    private static final List<JournalEntry> entries = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-zА-Яа-яІіЇїЄєҐґ]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?\\d{10,12}$");
    private static final Pattern HOUSE_PATTERN = Pattern.compile("^[0-9A-Za-z/]+$");
    private static final Pattern APARTMENT_PATTERN = Pattern.compile("^[0-9A-Za-z]+$");

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nCurator's Journal");
            System.out.println("1. Add new entry");
            System.out.println("2. Show all entries");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addEntry();
                    break;
                case "2":
                    showEntries();
                    break;
                case "3":
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void addEntry() {
        System.out.println("\nEnter student details:");

        String lastName = getValidInput("Last Name: ", NAME_PATTERN, "Name must contain only letters.");
        String firstName = getValidInput("First Name: ", NAME_PATTERN, "Name must contain only letters.");
        LocalDate dateOfBirth = getValidDate("Date of Birth (dd.MM.yyyy): ");
        String phoneNumber = getValidInput("Phone Number (+380123456789): ", PHONE_PATTERN, 
                                        "Phone must be 10-12 digits, may start with +.");
        String street = getValidInput("Street: ", NAME_PATTERN, "Street must contain only letters.");
        String houseNumber = getValidInput("House Number: ", HOUSE_PATTERN, 
                                         "House number must contain only letters, numbers, or /.");
        String apartment = getValidInput("Apartment: ", APARTMENT_PATTERN, 
                                       "Apartment must contain only letters or numbers.");

        JournalEntry entry = new JournalEntry(lastName, firstName, dateOfBirth, 
                                           phoneNumber, new Address(street, houseNumber, apartment));
        entries.add(entry);
        System.out.println("Entry added successfully!");
    }

    private static String getValidInput(String prompt, Pattern pattern, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (pattern.matcher(input).matches()) {
                return input;
            }
            System.out.println(errorMessage + " Try again.");
        }
    }

    private static LocalDate getValidDate(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                LocalDate date = LocalDate.parse(input, formatter);
                if (date.isAfter(LocalDate.now())) {
                    System.out.println("Date of birth cannot be in the future. Try again.");
                    continue;
                }
                return date;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Use dd.MM.yyyy. Try again.");
            }
        }
    }

    private static void showEntries() {
        if (entries.isEmpty()) {
            System.out.println("\nNo entries in the journal.");
            return;
        }
        System.out.println("\nJournal Entries:");
        for (int i = 0; i < entries.size(); i++) {
            System.out.println("\nEntry " + (i + 1) + ":");
            System.out.println(entries.get(i));
        }
    }
}