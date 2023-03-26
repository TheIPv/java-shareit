package ru.practicum.shareit.item;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
public class ItemControllerTest {
    @Autowired
    private MockMvc mvc;

    Gson gson = new Gson();

    @MockBean
    private ItemController itemController;

    @Test
    public void addItemTest() throws Exception {
        ItemDto itemToAdd = new ItemDto();
        itemToAdd.setName("item1");
        itemToAdd.setDescription("desc 1");
        itemToAdd.setAvailable(true);

        itemController.addItem(1L, itemToAdd);
        when(itemController.addItem(1L, itemToAdd))
                .thenReturn(itemToAdd);

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(itemToAdd))
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(gson.toJson(itemToAdd)));
    }
}