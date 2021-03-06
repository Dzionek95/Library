package com.bartek.library.service.book;

import com.bartek.library.model.book.Book;
import com.bartek.library.model.book.NewBookPurchaseRequest;
import com.bartek.library.repository.book.BookRepository;
import com.bartek.library.repository.book.NewBookPurchaseRequestRepository;
import com.bartek.library.service.SecurityUtilities;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Bartlomiej Janik
 * @since 1/10/2018
 */

@RunWith(MockitoJUnitRunner.class)
public class NewBookPurchaseRequestServiceTest {

    @Mock
    private NewBookPurchaseRequestRepository newBookPurchaseRequestRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private SecurityUtilities securityUtilities;

    @InjectMocks
    private NewBookPurchaseRequestService newBookPurchaseRequestService;

    @Test
    public void shouldReturnOneNewBookPurchaseRequests() {
        //given
        LocalDateTime dummyTime = LocalDateTime.parse("2018-01-10 20:59:42", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Book dummyBook = Book.builder()
                .author("Henryk Sienkiewicz")
                .title("Krzyżacy")
                .category("powieść historyczna")
                .available(true)
                .build();


        NewBookPurchaseRequest dummyPurchaseRequest =
                NewBookPurchaseRequest
                        .builder()
                        .dateOfRequest(dummyTime)
                        .approved(false)
                        .author("Adam Malysz")
                        .title("Skoki narciarskie na 100%")
                        .category("Sport")
                        .requestedBy("Zbyszek")
                        .build();

        //when
        when(bookRepository.findByAuthorIgnoreCaseContaining(any())).thenReturn(Collections.singletonList(dummyBook));
        when(newBookPurchaseRequestRepository.findAll()).thenReturn(Collections.emptyList());
        when(securityUtilities.retrieveNameFromAuthentication()).thenReturn("Zbyszek");
        //then
        Assert.assertEquals(dummyPurchaseRequest, newBookPurchaseRequestService.createNewBookRequest(dummyPurchaseRequest));
    }

}
