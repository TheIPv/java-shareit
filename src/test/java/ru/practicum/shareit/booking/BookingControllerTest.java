package ru.practicum.shareit.booking;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
public class BookingControllerTest {
    @Autowired
    private MockMvc mvc;

    Gson gson = new Gson();

    @MockBean
    private BookingController bookingController;

    @Test
    public void whenAddingBookingWithNonExistUser() throws Exception {
        BookingDtoCreate bookingDtoCreate = new BookingDtoCreate();
        bookingDtoCreate.setStart(LocalDateTime.now());
        bookingDtoCreate.setEnd(LocalDateTime.now());
        bookingDtoCreate.setItemId(1L);

        Booking booking = BookingMapper.toBookingFromDtoCreate(bookingDtoCreate, 1L, new User(), new Item());
        BookingDto bookingDto = BookingMapper.toBookingDto(booking);

        bookingController.addBooking(1L, bookingDtoCreate);
        when(bookingController.addBooking(1L, bookingDtoCreate))
                .thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(bookingDtoCreate))
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isBadRequest());
    }
}
