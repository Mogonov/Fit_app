package by.mogonov.foodtracker.users;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserService implements UserService {
    private final UserRepository usersRepo;
    private final PasswordEncoder passwordEncoder;

    public DefaultUserService(UserRepository usersRepo, PasswordEncoder passwordEncoder) {
        this.usersRepo = usersRepo;
        this.passwordEncoder = passwordEncoder;
    }


    public void save(User user) {
        usersRepo.save(user);
    }


    public User findByLogin(String login) {
        return usersRepo.findByLogin(login);
    }


    public User findByLoginAndPassword(String login, String password) {
        User user = usersRepo.findByLogin(login);
        if (user != null) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    public Page<User> getAll(Pageable pageable) {
        return usersRepo.findAll(pageable);
    }


}
