package ru.practicum.shareit.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L,"test", "test@test.test");
        reset(userRepository);
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void whenCheckAddMethod() {
        when(userRepository.save(user)).thenReturn(user);

        User testUser = UserMapper.toUser(userService.addUser(UserMapper.toUserDto(user)));
        assertEquals(1L, testUser.getId());
        assertEquals("test", testUser.getName());
        assertEquals("test@test.test", testUser.getEmail());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void whenCheckoutGetByIdMethod() {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        User testUser = UserMapper.toUser(userService.getUserById(1L));
        assertEquals(1L, testUser.getId());
        assertEquals("test", testUser.getName());
        assertEquals("test@test.test", testUser.getEmail());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void whenCheckoutGetAllMethod() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        //test
        List<User> users = userService.getAllUsers()
                .stream()
                .map(UserMapper::toUser)
                .collect(Collectors.toList());
        assertEquals(1, users.size());
        assertEquals(user, users.get(0));

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void whenCheckoutUpdateMethod() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);


        User testUser = UserMapper.toUser(userService.updateUser(1L, UserMapper.toUserDto(user)));
        assertEquals(1L, testUser.getId());
        assertEquals("test", testUser.getName());
        assertEquals("test@test.test", testUser.getEmail());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void whenCheckoutDeleteMethod() {
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

}