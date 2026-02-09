package com.unibuc.book_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorUpdateDto {
    private String firstName;
    private String lastName;
    private LocalDate debutDate;
}