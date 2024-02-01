package api.users.srpingbootHW.service;


import api.users.srpingbootHW.entity.User;
import api.users.srpingbootHW.exception.ProjectException;
import api.users.srpingbootHW.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //все юзеры
    public List<User> getUser() {
        return userRepository.findAll();
    }

    // юзер по айди
    public User oneUser(int id) {
        return userRepository.findById(id).stream()
                .findAny()
                .orElseThrow(() -> new ProjectException("user with id " + id + " not found in database"));
    }

    //проверка логина и пароля (кастомный метод репозитория)
    public User checkLoginAndPassword(User user) {
        User chekUser = userRepository.findByLoginAndPassword(user.getLogin(), user.getPassword());
        if (chekUser != null) {
            return chekUser;
        } else {
            throw new ProjectException("login or password incorrectly");
        }
    }

    //смена пароля
    public void changePassword(User user) {
        User checkUser = userRepository.findByLoginAndPassword(user.getLogin(), user.getPassword());
        if (checkUser != null) {
            checkUser.setPassword(user.getNewPassword());
            userRepository.save(checkUser);
        } else {
            throw new ProjectException("old password incorrectly");
        }
    }

    // сохранение юзера (регистрация)
    public void saveUser(User user) {
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ProjectException("login is taken");
        }
    }


}
