package util;

import javafx.scene.control.Button;

import java.time.LocalDate;
import java.util.Date;

public class Return {
    private int borrowId;
    private String name;
    private String isbn;
    private String book;
    private Date borrowDate;
    private Date returnDate;
    private Double lateFee;
    private String status;
    private Button button;
    private Date returnedDate;

    public Return(int borrowId, String name, String isbn, String book, Date borrowDate, Date returnDate, Date returnedDate, Double lateFee, String status) {
        this.borrowId = borrowId;
        this.name = name;
        this.isbn = isbn;
        this.book = book;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.returnedDate = returnedDate;
        this.lateFee = lateFee;
        this.status = status;
    }

    public Return(int borrowId, String name, String isbn, String book, Date borrowDate, Date returnDate, String status, Button button) {
        this.borrowId = borrowId;
        this.name = name;
        this.isbn = isbn;
        this.book = book;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
        this.button = button;
    }

    public int getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
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

    public Double getLateFee() {
        return lateFee;
    }

    public void setLateFee(Double lateFee) {
        this.lateFee = lateFee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Date getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(Date returnedDate) {
        this.returnedDate = returnedDate;
    }
}
