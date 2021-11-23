package by.mogonov.foodtracker.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User findByLogin(String login);
    User findByLoginAndPassword(String login, String password);
    void save(User user);
    Page<User> getAll (Pageable pageable);


}
