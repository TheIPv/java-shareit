package ru.practicum.shareit.item;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @Test
    public void updateItem() throws Exception {
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
                .andExpect(status().isOk());

        itemToAdd.setName("updatedName");
        itemController.updateItem(1L, itemToAdd, 1L);
        when(itemController.updateItem(1L, itemToAdd, 1L))
                .thenReturn(itemToAdd);

        mvc.perform(MockMvcRequestBuilders.patch("/items/1")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(itemToAdd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(itemToAdd.getName()));
    }

    @Test
    public void getItemById() throws Exception {
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
                .andExpect(status().isOk());

        itemController.getItemById(itemToAdd.getId(), 1L);
        when(itemController.getItemById(itemToAdd.getId(), 1L))
                .thenReturn(itemToAdd);

        mvc.perform(MockMvcRequestBuilders.get("/items/1")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}