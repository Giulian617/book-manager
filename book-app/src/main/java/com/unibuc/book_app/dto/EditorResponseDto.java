package com.unibuc.book_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EditorResponseDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private LocalDate debutDate;
}