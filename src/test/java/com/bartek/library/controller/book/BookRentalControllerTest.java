package com.bartek.library.controller.book;

import com.bartek.library.model.accounts.Account;
import com.bartek.library.model.accounts.Role;
import com.bartek.library.model.book.Book;
import com.bartek.library.model.book.BookRental;
import com.bartek.library.service.book.rental.RentBookService;
import com.bartek.library.service.book.rental.ReturnBookService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookRentalController.class, secure = false)
@RunWith(SpringRunner.class)
public class BookRentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReturnBookService returnBookService;

    @MockBean
    private RentBookService rentBookService;

    @Test
    public void shouldReturnOneBookRental() throws Exception {
        //given
        LocalDateTime dummyTime = LocalDateTime.parse("2018-01-10 20:59:42", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Account dummyAccount = Account
                .builder()
                .username("username")
                .password("password")
                .role(Role.ROLE_READER)
                .enabled(true)
                .build();

        Book dummyBook = Book.builder()
                .author("Henryk Sienkiewicz")
                .title("Krzyżacy")
                .category("powieść historyczna")
                .available(false)
                .build();
        BookRental dummyBookRental = BookRental.builder()
                .book(dummyBook)
                .dateOfRental(dummyTime)
                .returnDate(dummyTime.plusMonths(1))
                .account(dummyAccount)
                .build();

        String response = "{\"id\":0,\"account\":{\"id\":0,\"username\":\"username\",\"password\":\"password\",\"role\":\"ROLE_READER\",\"enabled\":true},\"book\":{\"id\":0,\"title\":\"Krzyżacy\",\"author\":\"Henryk Sienkiewicz\",\"category\":\"powieść historyczna\",\"available\":false},\"dateOfRental\":\"2018-01-10 20:59:42\",\"returnDate\":\"2018-02-10 20:59:42\"}";
        //when
        when(rentBookService.rentBook(any())).thenReturn(dummyBookRental);
        //then
        mockMvc.perform(post("/rental/rent?id=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        Assert.assertEquals(response, mockMvc.perform(post("/rental/rent?id=1"))
                .andReturn()
                .getResponse()
                .getContentAsString());
        verify(rentBookService, times(2)).rentBook(any());
    }

    @Test
    public void shouldReturnOneBook() throws Exception {
        //given
        Book dummyBook = Book.builder()
                .author("Henryk Sienkiewicz")
                .title("Krzyżacy")
                .category("powieść historyczna")
                .available(true)
                .build();
        String response = "{\"id\":0,\"title\":\"Krzyżacy\",\"author\":\"Henryk Sienkiewicz\",\"category\":\"powieść historyczna\",\"available\":true}";
        //when
        when(returnBookService.returnBook(any())).thenReturn(dummyBook);
        //then

        mockMvc.perform(post("/rental/return?id=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        Assert.assertEquals(response, mockMvc.perform(post("/rental/return?id=1"))
                .andReturn()
                .getResponse()
                .getContentAsString());
        verify(returnBookService, times(2)).returnBook(any());
    }

}

