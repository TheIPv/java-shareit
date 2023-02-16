package ru.practicum.shareit.user;

import java.util.Collection;

public interface UserStorage {
    Collection<User> getAllUsers();

    User getUserById(Long userId);

    User addUser(User user);

    User updateUser(User user);

    void deleteUser(Long userId);
}

