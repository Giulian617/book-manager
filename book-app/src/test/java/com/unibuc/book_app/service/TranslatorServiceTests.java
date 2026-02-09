package com.unibuc.book_app.service;

import com.unibuc.book_app.dto.BookSummaryDto;
import com.unibuc.book_app.dto.TranslatorCreateDto;
import com.unibuc.book_app.dto.TranslatorResponseDto;
import com.unibuc.book_app.dto.TranslatorUpdateDto;
import com.unibuc.book_app.exception.NotFoundException;
import com.unibuc.book_app.mapper.BookMapper;
import com.unibuc.book_app.mapper.TranslatorMapper;
import com.unibuc.book_app.model.Book;
import com.unibuc.book_app.model.Translator;
import com.unibuc.book_app.repository.TranslatorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TranslatorServiceTests {

    @Mock
    private TranslatorRepository translatorRepository;

    @Mock
    private TranslatorMapper translatorMapper;

    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private TranslatorService translatorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllTranslators_Valid() {
        Translator translator1 = Translator.builder()
                .firstName("John")
                .lastName("Doe")
                .build();
        Translator translator2 = Translator.builder()
                .firstName("Leo")
                .lastName("Tolstoy")
                .build();
        TranslatorResponseDto dto1 = new TranslatorResponseDto(1, "John", "Doe");
        TranslatorResponseDto dto2 = new TranslatorResponseDto(2, "Leo", "Tolstoy");

        when(translatorRepository.findAll()).thenReturn(List.of(translator1, translator2));
        when(translatorMapper.toResponseDto(translator1)).thenReturn(dto1);
        when(translatorMapper.toResponseDto(translator2)).thenReturn(dto2);

        List<TranslatorResponseDto> result = translatorService.findAllTranslators();

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
    }

    @Test
    void testFindAllBooksByTranslatorId_Valid() {
        Book book1 = Book.builder()
                .name("Intre iadul deznadejdii si iadul smereniei")
                .isbn("973-9344-56-9")
                .noPages(302)
                .price(27)
                .language("romanian")
                .publishDate(LocalDate.of(2024, 3, 10))
                .translator(
                        Translator.builder()
                                .firstName("John")
                                .lastName("Doe")
                                .build()
                )
                .build();
        Book book2 = Book.builder()
                .name("The Shadow of the Wind")
                .isbn("978-0143126393")
                .noPages(450)
                .price(15)
                .language("english")
                .publishDate(LocalDate.of(2023, 11, 5))
                .translator(
                        Translator.builder()
                                .firstName("Leo")
                                .lastName("Tolstoy")
                                .build()
                )
                .build();
        BookSummaryDto dto1 = new BookSummaryDto(1, "1984");
        BookSummaryDto dto2 = new BookSummaryDto(2, "Animal Farm");
        Translator translator = Translator.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .books(List.of(book1, book2))
                .build();

        when(translatorRepository.findById(1)).thenReturn(Optional.of(translator));
        when(bookMapper.toSummaryDto(book1)).thenReturn(dto1);
        when(bookMapper.toSummaryDto(book2)).thenReturn(dto2);

        List<BookSummaryDto> result = translatorService.findAllBooksByTranslatorId(1);

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
    }

    @Test
    void testFindTranslatorEntityById_Valid() {
        Translator translator = Translator.builder()
                .firstName("John")
                .lastName("Doe")
                .build();
        when(translatorRepository.findById(1)).thenReturn(Optional.of(translator));

        Translator result = translatorService.findTranslatorEntityById(1);

        assertEquals(translator, result);
    }

    @Test
    void testFindTranslatorEntityById_Invalid() {
        when(translatorRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> translatorService.findTranslatorEntityById(1));

        assertEquals("Translator with id 1 not found", exception.getMessage());
    }

    @Test
    void testFindTranslatorById_Valid() {
        Translator translator = Translator.builder()
                .firstName("John")
                .lastName("Doe")
                .build();
        TranslatorResponseDto dto = new TranslatorResponseDto(1, "John", "Doe");

        when(translatorRepository.findById(1)).thenReturn(Optional.of(translator));
        when(translatorMapper.toResponseDto(translator)).thenReturn(dto);

        TranslatorResponseDto result = translatorService.findTranslatorById(1);

        assertEquals(dto, result);
    }

    @Test
    void testCreateTranslator_Valid() {
        TranslatorCreateDto createDto = new TranslatorCreateDto("John", "Doe");
        Translator translator = Translator.builder()
                .firstName("John")
                .lastName("Doe")
                .build();
        Translator savedTranslator = Translator.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .build();
        TranslatorResponseDto responseDto = new TranslatorResponseDto(1, "John", "Doe");

        when(translatorMapper.toEntity(createDto)).thenReturn(translator);
        when(translatorRepository.save(translator)).thenReturn(savedTranslator);
        when(translatorMapper.toResponseDto(savedTranslator)).thenReturn(responseDto);

        TranslatorResponseDto result = translatorService.createTranslator(createDto);

        assertEquals(responseDto, result);
    }

    @Test
    void testUpdateTranslator_Valid() {
        TranslatorUpdateDto updateDto = new TranslatorUpdateDto("John", "Doe");
        Translator translator = Translator.builder()
                .firstName("John")
                .lastName("Doe")
                .build();
        Translator updatedTranslator = Translator.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .build();
        TranslatorResponseDto responseDto = new TranslatorResponseDto(1, "John", "Doe");

        when(translatorRepository.findById(1)).thenReturn(Optional.of(translator));
        doNothing().when(translatorMapper).updateEntityFromDto(updateDto, translator);
        when(translatorRepository.save(translator)).thenReturn(updatedTranslator);
        when(translatorMapper.toResponseDto(updatedTranslator)).thenReturn(responseDto);

        TranslatorResponseDto result = translatorService.updateTranslator(1, updateDto);

        assertEquals(responseDto, result);
    }

    @Test
    void testUpdateTranslator_Invalid() {
        TranslatorUpdateDto updateDto = new TranslatorUpdateDto("John", "Doe");

        when(translatorRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> translatorService.updateTranslator(1, updateDto));

        assertEquals("Translator with id 1 not found", exception.getMessage());
    }

    @Test
    void testDeleteTranslator_Valid() {
        doNothing().when(translatorRepository).deleteById(1);

        translatorService.deleteTranslator(1);

        verify(translatorRepository, times(1)).deleteById(1);
    }
}
