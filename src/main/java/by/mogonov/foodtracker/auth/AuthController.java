package by.mogonov.foodtracker.auth;

import by.mogonov.foodtracker.security.JwtProvider;
import by.mogonov.foodtracker.users.User;
import by.mogonov.foodtracker.users.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthController(UserService userService, JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthCredentials creds) {
        User user = new User();
        user.setLogin(creds.getLogin());
        user.setRole(Roles.ROLE_USER);
        user.setPassword(passwordEncoder.encode(creds.getPassword()));
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/auth")
    public String auth(@RequestBody AuthCredentials creds) {
        User userEntity = userService.findByLoginAndPassword(creds.getLogin(), creds.getPassword());
        return jwtProvider.generateToken(userEntity.getLogin());
    }
}