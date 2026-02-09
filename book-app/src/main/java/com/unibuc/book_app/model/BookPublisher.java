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
@Table(name = "book_publisher")
public class BookPublisher {
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class BookPublisherId implements Serializable {
        private Integer bookId;
        private Integer publisherId;
    }

    @EmbeddedId
    private BookPublisherId bookPublisherId;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @MapsId("publisherId")
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;
}
