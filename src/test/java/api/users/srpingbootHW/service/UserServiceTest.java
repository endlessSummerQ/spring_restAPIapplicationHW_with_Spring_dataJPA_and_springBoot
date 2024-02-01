package api.users.srpingbootHW.service;

import api.users.srpingbootHW.entity.User;
import api.users.srpingbootHW.exception.ProjectException;
import api.users.srpingbootHW.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    //получаем всех юзеров
    @Test
    void getUser_withUser_userList() {
        List<User> userList = List.of(new User("user", "user"),
                new User("user2", "user2"));
        Mockito.doReturn(userList).when(userRepository).findAll();
        assertEquals(userList, userService.getUser());
    }

    //получаем пустой лист
    @Test
    void getUser_withoutUsers_emptyList() {
        List<User> emptyList = new ArrayList<>();
        Mockito.doReturn(emptyList).when(userRepository).findAll();

        assertEquals(emptyList, userService.getUser());
    }

    //получаем юзера по айди
    //нет юзера
    @Test
    void oneUser_notIsiInTheDatabase_getException() {
        Mockito.doThrow(ProjectException.class).when(userRepository).findById(0);
        assertThrows(ProjectException.class, () -> userService.oneUser(0));
    }

    // есть юзер
    @Test
    @Disabled("тест в процессе разработки -)")
    void oneUser_isiInTheDatabase_getUser() {
    }

    //логин
    //успешный логин
    @Test
    void checkLoginAndPassword_loginAndPasswordMatch_getUser() {
        User user = new User("user", "user");
        String dataBaseLogin = "user";
        String dataBasePassword = "user";
        Mockito.doAnswer(invocationOnMock -> {
            if (user.getLogin().equals(dataBaseLogin) &&
                    user.getPassword().equals(dataBasePassword)) {
                return user;
            }
            return null;
        }).when(userRepository).findByLoginAndPassword(user.getLogin(), user.getPassword());

        assertEquals(user, userService.checkLoginAndPassword(user));
    }

    //логина или пароль введен неверно
    @Test
    void checkLoginAndPassword_loginAndPasswordNotMatch_getException() {
        User user = new User("user1", "user");
        String dataBaseLogin = "user";
        String dataBasePassword = "user";
        Mockito.doAnswer(invocationOnMock -> {
            if (user.getLogin().equals(dataBaseLogin) &&
                    user.getPassword().equals(dataBasePassword)) {
                return user;
            }
            throw new ProjectException("abc");
        }).when(userRepository).findByLoginAndPassword(user.getLogin(), user.getPassword());

        assertThrows(ProjectException.class, () -> userService.checkLoginAndPassword(user));
    }

    //смена пароля
    //успешная смена пароля
    @Test
    void changePassword_oldPasswordCorrect_change() {
        User user = new User("user", "user");
        String loginInDataBase = "user";
        String passwordInDataBase = "user";
        Mockito.doAnswer(invocationOnMock -> {
            if (user.getLogin().equals(loginInDataBase) &&
                    user.getPassword().equals(passwordInDataBase)) {
                return user;
            }
            return null;
        }).when(userRepository).findByLoginAndPassword(user.getLogin(), user.getPassword());
        User result = userRepository.findByLoginAndPassword(user.getLogin(), user.getPassword());
        userService.changePassword(result);

        Mockito.verify(userRepository).save(result);
    }

    // старый пароль не совпадает (неверный логин)
    @Test
    void changePassword_oldPasswordNotCorrect_exception() {
        User user = new User("user1", "user");
        String loginInDataBase = "user";
        String passwordInDataBase = "user";
        Mockito.doAnswer(invocationOnMock -> {
            if (user.getLogin().equals(loginInDataBase) &&
                    user.getPassword().equals(passwordInDataBase)) {
                return user;
            }
            throw new ProjectException("abc");
        }).when(userRepository).findByLoginAndPassword(user.getLogin(), user.getPassword());

        assertThrows(ProjectException.class, () -> userService.changePassword(user));
    }

    //регистрация
    //логин совпал с имеющимся в бд -> Exception
    @Test
    void saveUser_loginMatchWithLoginInDataBase_getException() {
        User user = new User("user", "user");
        String loginInDataBase = "user";
        Mockito.doAnswer(invocationOnMock -> {
            if (user.getLogin().equals(loginInDataBase)) {
                throw new ProjectException("abc");
            }
            return user;
        }).when(userRepository).save(user);

        assertThrows(ProjectException.class, () -> userService.saveUser(user));
    }

    //успешная регистрация
    @Test
    void saveUser_loginNotMatchWithLoginInDataBase_saveUserInDataBase() {
        User user = new User("user1", "user");
        String loginInDataBase = "user";
        Mockito.doAnswer(invocationOnMock -> {
            if (user.getLogin().equals(loginInDataBase)) {
                throw new ProjectException("abc");
            }
            return user;
        }).when(userRepository).save(user);
        userRepository.save(user);

        Mockito.verify(userRepository).save(user);
    }
}