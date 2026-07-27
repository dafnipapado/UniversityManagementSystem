package io.github.dafnipapado.university.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            .authorizeHttpRequests(request -> request
                    .requestMatchers("/index").permitAll()
                    .requestMatchers("/login").permitAll()
                    .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                    .requestMatchers("/users/**").hasAuthority("ROLE_ADMIN")
                    .requestMatchers(HttpMethod.GET, "/teachers", "/teachers/", "/teachers/index").hasAuthority("ROLE_TEACHER")
                    .requestMatchers("/teachers/create", "/teachers/success").hasAuthority("INSERT_TEACHER")
                    .requestMatchers("/teachers/edit/{uuid}", "/teachers/edit", "/teachers/update-success").hasAuthority("EDIT_TEACHER")
                    .requestMatchers(HttpMethod.POST, "/teachers/delete/{uuid}").hasAuthority("DELETE_TEACHER")
                    .requestMatchers(HttpMethod.GET, "/teachers/view").hasAuthority("ROLE_ADMIN")
                    .requestMatchers(HttpMethod.GET, "/students", "/students/", "/students/index").hasAuthority("ROLE_STUDENT")
                    .requestMatchers("/students/create", "/students/success").hasAuthority("INSERT_STUDENT")
                    .requestMatchers("/students/edit/{uuid}", "/students/edit", "/students/update-success").hasAuthority("EDIT_STUDENT")
                    .requestMatchers(HttpMethod.POST, "/students/delete/{uuid}").hasAuthority("DELETE_STUDENT")
                    .requestMatchers(HttpMethod.GET, "/students/view").hasAuthority("ROLE_ADMIN")
                    .requestMatchers("/courses/create", "/courses/success").hasAuthority("INSERT_COURSE")
                    .requestMatchers("/courses/edit/{uuid}", "/courses/edit", "/courses/update-success").hasAuthority("EDIT_COURSE")
                    .requestMatchers(HttpMethod.POST, "/courses/delete/{uuid}").hasAuthority("DELETE_COURSE")
                    .requestMatchers(HttpMethod.GET, "/courses/view").hasAuthority("ROLE_ADMIN")
                    .requestMatchers("/semesters/**").hasAuthority("ROLE_ADMIN")
                    .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                    .loginPage("/login")
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
            )
            .logout((logout) -> logout
                    .logoutSuccessUrl("/login?logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")

            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

}
