package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NoSuchItemException;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private Long uniqueId = Long.valueOf(1);
    private List<User> users = new LinkedList<>();

    @Override
    public List<User> getAllUsers() {
        return users;
    }

    @Override
    public User getUserById(Long userId) {
        for (User currentUser: users) {
            if (currentUser.getId().equals(userId)) {
                return currentUser;
            }
        }
        throw new NoSuchItemException("User with ID " + userId + " doesn't exist");
    }

    @Override
    public User addUser(User user) {
        checkIfEmailExist(user.getId(), user.getEmail());
        user.setId(uniqueId);
        ++uniqueId;
        users.add(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        for (User currentUser: users) {
            if (currentUser.getId().equals(user.getId())) {
               if (user.getEmail() != null) {
                   checkIfEmailExist(user.getId(), user.getEmail());
                   currentUser.setEmail(user.getEmail());
               }
               if (user.getName() != null) currentUser.setName(user.getName());
               return currentUser;
            }
        }
        throw new NoSuchItemException("This user doesn't exist");
    }

    @Override
    public void deleteUser(Long userId) {
        for (User currentUser: users) {
            if (currentUser.getId().equals(userId)) {
                users.remove(currentUser);
                return;
            }
        }
        throw new NoSuchItemException("This user doesn't exist");
    }

    private boolean checkIfEmailExist(Long userId, String email) {
        if (!users.isEmpty()) {
            for (User currentUser : users) {
                if(currentUser.getId() == userId) continue;
                if (currentUser.getEmail().equals(email)) {
                    log.trace("[X] User with email _{} is already exist", currentUser.getEmail());
                    throw new AlreadyExistException("User with this email is already exist");
                }
            }
        }
        return false;
    }
}
