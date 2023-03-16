package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotValidException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final UserMapper userMapper;


    @Override
    public List<UserDto> getAllUsers() {
        log.debug("Showing all users");
        return userStorage.getAllUsers()
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId) {
        try {
            return userMapper.toUserDto(userStorage.getUserById(userId));
        } catch (NoSuchElementException e) {
            log.trace("[X] User with _{} ID doesn't exist", userId);
        }
        throw new RuntimeException();
    }

    @Override
    public UserDto addUser(UserDto user) {
        if (user.getEmail() == null || user.getName() == null) {
            throw new NotValidException("Incorrect user data");
        }
        checkValid(user);
        User addedUser = userStorage.addUser(userMapper.toUser(user));
        log.debug("[V] User witch ID _{} added", user.getId());
        return userMapper.toUserDto(addedUser);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto user) {

        user.setId(userId);
        User updatedUser = userStorage.updateUser(userMapper.toUser(user));
        log.debug("[V] User witch ID _{} successfully updated", user.getId());
        return userMapper.toUserDto(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        userStorage.deleteUser(userId);
        log.debug("[V] User witch ID _{} successfully deleted", userId);
    }

    private boolean checkValid(UserDto user) {
        if (user.getEmail() != null && (user.getEmail().isEmpty()
                || user.getEmail().isBlank() || !user.getEmail().contains("@"))) {
            throw new NotValidException("Incorrect user data");
        }
        if (user.getName() != null && (user.getName().isBlank()
                || user.getName().isEmpty())) {
            throw new NotValidException("Incorrect user data");
        }
        return true;
    }
}
