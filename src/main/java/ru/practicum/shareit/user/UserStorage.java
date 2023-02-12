package ru.practicum.shareit.user;

import java.util.List;

public interface UserStorage {
    List<User> getAllUsers();
    User getUserById(Long userId);
    User addUser(User user);
    User updateUser(User user);
    void deleteUser(Long userId);
}

