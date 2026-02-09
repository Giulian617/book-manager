package com.unibuc.book_app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublisherCreateDto {
    @NotBlank(message = "name is required and cannot be blank")
    private String name;

    @NotNull(message = "foundedDate is required")
    @PastOrPresent(message = "foundedDate must be in the past or present")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate foundedDate;
}