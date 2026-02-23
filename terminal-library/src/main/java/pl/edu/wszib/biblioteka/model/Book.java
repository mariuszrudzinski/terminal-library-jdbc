package pl.edu.wszib.biblioteka.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Book {
    private int id;
    private String title;
    private String author;
    private int year;
    private boolean rent;

    public Book(int id, String title, String author, int year) {
        this(id, title, author, year, false);
    }

    public Book(String title, String author, int year) {

        this.title = title;
        this.author = author;
        this.year = year;
        this.rent = false;
    }
    @Override
    public String toString() {
        return new StringBuilder()
                .append(this.getId())
                .append("\tTytuł: ")
                .append(this.getTitle())
                .append("\tAutor: ")
                .append(this.getAuthor())
                .append("\tRok wydania: ")
                .append(this.getYear())
                .append("\tStatus: ")
                .append(this.isRent() ? "Rented" : "Available")
                .toString();
    }
}
