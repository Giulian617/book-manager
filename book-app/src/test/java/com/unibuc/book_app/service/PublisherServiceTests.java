package com.unibuc.book_app.service;

import com.unibuc.book_app.dto.BookSummaryDto;
import com.unibuc.book_app.dto.PublisherCreateDto;
import com.unibuc.book_app.dto.PublisherResponseDto;
import com.unibuc.book_app.dto.PublisherUpdateDto;
import com.unibuc.book_app.exception.NotFoundException;
import com.unibuc.book_app.mapper.BookMapper;
import com.unibuc.book_app.mapper.PublisherMapper;
import com.unibuc.book_app.model.Book;
import com.unibuc.book_app.model.Publisher;
import com.unibuc.book_app.model.Translator;
import com.unibuc.book_app.repository.BookPublisherRepository;
import com.unibuc.book_app.repository.PublisherRepository;
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

class PublisherServiceTests {

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private PublisherMapper publisherMapper;

    @Mock
    private BookPublisherRepository bookPublisherRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private PublisherService publisherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllPublishers_Valid() {
        Publisher publisher1 = Publisher.builder()
                .name("Nemira")
                .foundedDate(LocalDate.of(1931, 2, 18))
                .build();
        Publisher publisher2 = Publisher.builder()
                .name("Sophia")
                .foundedDate(LocalDate.of(1965, 7, 31))
                .build();
        PublisherResponseDto dto1 = new PublisherResponseDto(1, "Nemira", LocalDate.of(1931, 2, 18));
        PublisherResponseDto dto2 = new PublisherResponseDto(2, "Sophia", LocalDate.of(1965, 7, 31));

        when(publisherRepository.findAll()).thenReturn(List.of(publisher1, publisher2));
        when(publisherMapper.toResponseDto(publisher1)).thenReturn(dto1);
        when(publisherMapper.toResponseDto(publisher2)).thenReturn(dto2);

        List<PublisherResponseDto> result = publisherService.findAllPublishers();

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
    }

    @Test
    void testFindAllBooksByPublisherId_Valid() {
        Book book1 = Book.builder()
                .name("Intre iadul deznadejdii si iadul smereniei")
                .isbn("973-9344-56-9")
                .noPages(302)
                .price(27)
                .language("romanian")
                .publishDate(LocalDate.of(2024, 3, 10))
                .translator(
                        Translator.builder()
                                .id(1)
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
                                .id(2)
                                .firstName("Leo")
                                .lastName("Tolstoy")
                                .build()
                )
                .build();
        BookSummaryDto dto1 = new BookSummaryDto(1, "1984");
        BookSummaryDto dto2 = new BookSummaryDto(2, "Animal Farm");

        when(bookPublisherRepository.findAllBooksByPublisherId(1)).thenReturn(List.of(book1, book2));
        when(bookMapper.toSummaryDto(book1)).thenReturn(dto1);
        when(bookMapper.toSummaryDto(book2)).thenReturn(dto2);

        List<BookSummaryDto> result = publisherService.findAllBooksByPublisherId(1);

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
    }

    @Test
    void testFindPublisherEntityById_Valid() {
        Publisher publisher = Publisher.builder()
                .name("Nemira")
                .foundedDate(LocalDate.of(1931, 2, 18))
                .build();
        when(publisherRepository.findById(1)).thenReturn(Optional.of(publisher));

        Publisher result = publisherService.findPublisherEntityById(1);

        assertEquals(publisher, result);
    }

    @Test
    void testFindPublisherEntityById_Invalid() {
        when(publisherRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> publisherService.findPublisherEntityById(1));

        assertEquals("Publisher with id 1 not found", exception.getMessage());
    }

    @Test
    void testFindPublisherById_Valid() {
        Publisher publisher = Publisher.builder()
                .name("Nemira")
                .foundedDate(LocalDate.of(1931, 2, 18))
                .build();
        PublisherResponseDto dto = new PublisherResponseDto(1, "Nemira", LocalDate.of(1931, 2, 18));

        when(publisherRepository.findById(1)).thenReturn(Optional.of(publisher));
        when(publisherMapper.toResponseDto(publisher)).thenReturn(dto);

        PublisherResponseDto result = publisherService.findPublisherById(1);

        assertEquals(dto, result);
    }

    @Test
    void testCreatePublisher_Valid() {
        PublisherCreateDto createDto = new PublisherCreateDto("Nemira", LocalDate.of(1931, 2, 18));
        Publisher publisher = Publisher.builder()
                .name("Nemira")
                .foundedDate(LocalDate.of(1931, 2, 18))
                .build();
        Publisher savedPublisher = Publisher.builder()
                .id(1)
                .name("Nemira")
                .foundedDate(LocalDate.of(1931, 2, 18))
                .build();
        PublisherResponseDto responseDto = new PublisherResponseDto(1, "Nemira", LocalDate.of(1931, 2, 18));

        when(publisherMapper.toEntity(createDto)).thenReturn(publisher);
        when(publisherRepository.save(publisher)).thenReturn(savedPublisher);
        when(publisherMapper.toResponseDto(savedPublisher)).thenReturn(responseDto);

        PublisherResponseDto result = publisherService.createPublisher(createDto);

        assertEquals(responseDto, result);
    }

    @Test
    void testUpdatePublisher_Valid() {
        PublisherUpdateDto updateDto = new PublisherUpdateDto("Nemira", LocalDate.of(1931, 2, 18));
        Publisher publisher = Publisher.builder()
                .name("Nemira")
                .foundedDate(LocalDate.of(1931, 2, 18))
                .build();
        Publisher updatedPublisher = Publisher.builder()
                .id(1)
                .name("Nemira")
                .foundedDate(LocalDate.of(1931, 2, 18))
                .build();
        PublisherResponseDto responseDto = new PublisherResponseDto(1, "Nemira", LocalDate.of(1931, 2, 18));

        when(publisherRepository.findById(1)).thenReturn(Optional.of(publisher));
        doNothing().when(publisherMapper).updateEntityFromDto(updateDto, publisher);
        when(publisherRepository.save(publisher)).thenReturn(updatedPublisher);
        when(publisherMapper.toResponseDto(updatedPublisher)).thenReturn(responseDto);

        PublisherResponseDto result = publisherService.updatePublisher(1, updateDto);

        assertEquals(responseDto, result);
    }

    @Test
    void testUpdatePublisher_Invalid() {
        PublisherUpdateDto updateDto = new PublisherUpdateDto("Nemira", LocalDate.now());

        when(publisherRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> publisherService.updatePublisher(1, updateDto));

        assertEquals("Publisher with id 1 not found", exception.getMessage());
    }

    @Test
    void testDeletePublisher_Valid() {
        doNothing().when(publisherRepository).deleteById(1);

        publisherService.deletePublisher(1);

        verify(publisherRepository, times(1)).deleteById(1);
    }
}
