package com.bartek.library.service.book.rental;

import com.bartek.library.model.accounts.Account;
import com.bartek.library.model.accounts.Role;
import com.bartek.library.model.book.Book;
import com.bartek.library.model.book.BookRental;
import com.bartek.library.repository.admin.AccountRepository;
import com.bartek.library.repository.book.BookRentalRepository;
import com.bartek.library.repository.book.BookRepository;
import com.bartek.library.repository.notifications.PenaltyRepository;
import com.bartek.library.service.SecurityUtilities;
import com.bartek.library.service.notifications.UsersNotificationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReturnBookServiceTest {

    @Mock
    private BookRentalRepository rentBookRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private OrdersQueueService ordersQueueService;
    @Mock
    private SecurityUtilities securityUtilities;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UsersNotificationService usersNotificationService;
    @Mock
    private PenaltyRepository penaltyRepository;

    @InjectMocks
    private ReturnBookService returnBookService;

    @Test
    public void shouldReturnOneBookWithAvailabilitySetToTrue() {
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
        //when
        when(accountRepository.findOneByUsername(anyString())).thenReturn(dummyAccount);
        when(rentBookRepository.findOne(any())).thenReturn(dummyBookRental);
        when(penaltyRepository.findPenaltyByAccountId(anyLong())).thenReturn(null);
        //then
        Assert.assertEquals(true, returnBookService.returnBook(any()).isAvailable());
        verify(usersNotificationService, times(1)).notifyUserThatBookIsAbleToRent(anyLong());
    }

}
