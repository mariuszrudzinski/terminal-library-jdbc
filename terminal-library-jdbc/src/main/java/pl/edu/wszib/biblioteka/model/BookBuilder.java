package pl.edu.wszib.biblioteka.model;

public class BookBuilder {
    private int id;
    private String title;
    private String author;
    private int year;
    private boolean rent = false;

    public BookBuilder id(int id) {
        this.id = id;
        return this;
    }
    public BookBuilder title(String title) {
        this.title = title;
        return this;
    }

    public BookBuilder author(String author) {
        this.author = author;
        return this;
    }

    public BookBuilder year(int year) {
        this.year = year;
        return this;
    }

    public BookBuilder rent(boolean rent) {
        this.rent = rent;
        return this;
    }

    public Book build() {
        return new Book(id, title, author, year, rent);
    }
}

