package by.mogonov.foodtracker.security;

import by.mogonov.foodtracker.users.User;
import by.mogonov.foodtracker.users.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserHolder {

    private final UserService userService;

    public UserHolder(UserService userService) {
        this.userService = userService;
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public User getUser() {
        String login = getAuthentication().getName();
        return userService.findByLogin(login);
    }
}
