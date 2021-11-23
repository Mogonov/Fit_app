package by.mogonov.foodtracker.startup;

import by.mogonov.foodtracker.auth.Roles;
import by.mogonov.foodtracker.users.UserRepository;
import by.mogonov.foodtracker.users.User;
import by.mogonov.foodtracker.users.Status;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class Loader {

    private final UserRepository userDao;
    private final PasswordEncoder passwordEncoder;

    public Loader(UserRepository userDao,
                  PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void initAdmin() {
        User user = new User();
        user.setName("Администратор Администратович");
        user.setLogin("admin@mail.ru");
        user.setPassword(passwordEncoder.encode("111"));
        user.setRole(Roles.ROLE_ADMIN);
        user.setStatus(Status.ACTIVE);
        user.setCreationDate(LocalDateTime.now());
        user.setUpdateDate(user.getCreationDate());
        userDao.save(user);
    }
}
