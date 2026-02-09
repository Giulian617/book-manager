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
@Table(name = "book_author_editor")
public class BookAuthorEditor {
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class BookAuthorEditorId implements Serializable {
        private Integer bookId;
        private Integer authorId;
        private Integer editorId;
    }

    @EmbeddedId
    private BookAuthorEditorId bookAuthorEditorId;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @MapsId("authorId")
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne
    @MapsId("editorId")
    @JoinColumn(name = "editor_id")
    private Editor editor;
}
