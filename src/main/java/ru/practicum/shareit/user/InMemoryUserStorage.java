package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NoSuchItemException;

import java.util.*;

@Slf4j
@Component("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private Long uniqueId = Long.valueOf(1);
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User getUserById(Long userId) {
        if(users.containsKey(userId)) {
            return users.get(userId);
        }
        throw new NoSuchItemException("User with ID " + userId + " doesn't exist");
    }

    @Override
    public User addUser(User user) {
        checkIfEmailExist(user.getId(), user.getEmail());
        user.setId(uniqueId);
        ++uniqueId;
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if(users.containsKey(user.getId())) {
            User currentUser = users.get(user.getId());
            if (user.getEmail() != null) {
                checkIfEmailExist(user.getId(), user.getEmail());
                currentUser.setEmail(user.getEmail());
            }
            if (user.getName() != null) currentUser.setName(user.getName());
            return currentUser;
        }
        throw new NoSuchItemException("This user doesn't exist");
    }

    @Override
    public void deleteUser(Long userId) {
        if (users.containsKey(userId)) {
            users.remove(userId);
            return;
        }
        throw new NoSuchItemException("This user doesn't exist");
    }

    private boolean checkIfEmailExist(Long userId, String email) {
        if (!users.isEmpty()) {
            if (users.values().stream().anyMatch(user -> user.getEmail().equals(email))) {
                if (users.get(userId) != null && users.get(userId).getEmail().equals(email)) {
                   return false;
                }
                log.trace("[X] User with email _{} is already exist", email);
                throw new AlreadyExistException("User with this email is already exist");
            }
        }
        return false;
    }
}
