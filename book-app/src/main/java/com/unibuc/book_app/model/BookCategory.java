package com.unibuc.book_app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode()
@SuperBuilder
@Entity
@Table(name = "book_category")
public class BookCategory {
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class BookCategoryId implements Serializable {
        private Integer bookId;
        private Integer categoryId;
    }

    @EmbeddedId
    private BookCategoryId bookCategoryId;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    private Category category;
}
