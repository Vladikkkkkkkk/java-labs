package lab3.controller;

import lab3.controller.BookController;
import lab3.model.BookModel;
import lab3.view.BookView;

public class Main {
    public static void main(String[] args) {
        BookModel model = new BookModel();
        BookView view = new BookView();
        BookController controller = new BookController(model, view);
        
        controller.run();
    }
}