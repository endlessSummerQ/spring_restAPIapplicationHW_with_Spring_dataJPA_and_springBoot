package api.users.srpingbootHW.controller;


import api.users.srpingbootHW.entity.User;
import api.users.srpingbootHW.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //все юзеры
    @GetMapping("/users")
    public List<User> getAllUser() {
        return userService.getUser();
    }

    // один юзер
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") int id) {
        return userService.oneUser(id);
    }

    //регистрация
    @PostMapping("/users")
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        userService.saveUser(user);
        return new ResponseEntity<>("register successfully", HttpStatus.OK);
    }

    //логин
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        userService.checkLoginAndPassword(user);
        return new ResponseEntity<>("login successfully", HttpStatus.OK);
    }

    //смена пароля
    @PostMapping("/change")
    public ResponseEntity<String> changePassword(@RequestBody User user) {
        userService.changePassword(user);
        return new ResponseEntity<>("password change successfully", HttpStatus.OK);
    }


}
