package util;

import javafx.scene.control.Button;

import java.util.Date;

public class Notification {
    private String name;
    private String book;
    private String isbn;
    private Date borrowDate;
    private Date returnDate;
    private Button button;

    public Notification(String name, String isbn, String book, Date borrowDate, Date returnDate, Button button) {
        this.name = name;
        this.book = book;
        this.isbn = isbn;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.button = button;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }
}
