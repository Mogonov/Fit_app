package by.mogonov.foodtracker.security;


import by.mogonov.foodtracker.auth.Roles;
import by.mogonov.foodtracker.security.filters.AuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthFilter authFilter;

    WebSecurityConfig(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/products/**", "/api/recipes/**").hasAnyRole(Roles.ROLE_USER.getValue(), Roles.ROLE_ADMIN.getValue())
                .antMatchers("/api/profiles/**").hasAnyRole(Roles.ROLE_USER.getValue(), Roles.ROLE_ADMIN.getValue())
                .antMatchers("/api/**").hasRole(Roles.ROLE_ADMIN.getValue())
                .antMatchers("/register", "/auth").anonymous()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}