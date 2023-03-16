package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NoSuchItemException;
import ru.practicum.shareit.exception.NotValidException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private Long userId = Long.valueOf(1);


    @Override
    public List<UserDto> getAllUsers() {
        log.debug("Showing all users");
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId) {
        try {
            return UserMapper.toUserDto(userRepository.findById(userId).orElseThrow());
        } catch (NoSuchElementException e) {
            log.trace("[X] User with _{} ID doesn't exist", userId);
        }
        throw new NoSuchItemException("[X] User with " + userId + "ID doesn't exist");
    }

    @Override
    public UserDto addUser(UserDto user) {
        if (user.getEmail() == null || user.getName() == null) {
            throw new NotValidException("Incorrect user data");
        }
        checkValid(user);
        user.setId(userId);
        ++userId;
        User addedUser = userMapper.toUser(user);
        userRepository.save(addedUser);
        log.debug("[V] User witch ID _{} added", user.getId());
        return userMapper.toUserDto(addedUser);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto user) {
        checkValid(user);
        User updatedUser = userRepository.findById(userId).orElseThrow();
        if(user.getName() != null) updatedUser.setName(user.getName());
        if(user.getEmail() != null) updatedUser.setEmail(user.getEmail());
        userRepository.save(updatedUser);
        log.debug("[V] User witch ID _{} successfully updated", user.getId());
        return userMapper.toUserDto(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
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
