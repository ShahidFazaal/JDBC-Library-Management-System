package util;

public class Book {
    private String isbnNumber;
    private String title;
    private String author;

    public Book(String isbnNumber, String title, String author) {
        this.isbnNumber = isbnNumber;
        this.title = title;
        this.author = author;
    }

    public String getIsbnNumber() {
        return isbnNumber;
    }

    public void setIsbnNumber(String isbnNumber) {
        this.isbnNumber = isbnNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return isbnNumber;
    }
}
