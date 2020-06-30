package util;

import java.time.LocalDate;
import java.util.Date;

public class Borrow {
    private int borrowId;
    private String  nic;
    private String name;
    private int phone;
    private String isbn;
    private String book;
    private LocalDate date;
    private LocalDate returnDate;

    public Borrow(int borrowId, String nic, String name, int phone, LocalDate date, LocalDate returnDate) {
        this.borrowId = borrowId;
        this.nic = nic;
        this.name = name;
        this.phone = phone;
        this.date = date;
        this.returnDate = returnDate;
    }

    public Borrow(String isbn, String book) {
        this.isbn = isbn;
        this.book = book;
    }

    public int getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
