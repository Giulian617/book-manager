package com.unibuc.book_app.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode()
@Builder
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String isbn;

    @Column(nullable = false)
    private Integer noPages;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private LocalDate publishDate;

    @ManyToOne
    @JoinColumn(name = "translator_id")
    private Translator translator;

    @OneToMany(mappedBy = "book")
    private List<BookCategory> bookCategories;

    @OneToMany(mappedBy = "book")
    private List<BookPublisher> bookPublishers;

    @OneToMany(mappedBy = "book")
    private List<BookAuthorEditor> bookAuthorEditors;
}
