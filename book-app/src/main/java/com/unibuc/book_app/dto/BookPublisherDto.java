package com.unibuc.book_app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BookPublisherDto {
    @NotNull
    private Integer bookId;

    @NotNull
    private Integer publisherId;
}
