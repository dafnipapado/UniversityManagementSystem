package io.github.dafnipapado.university.runner;

import io.github.dafnipapado.university.exception.EntityNotFoundException;
import io.github.dafnipapado.university.model.User;
import io.github.dafnipapado.university.model.static_data.Role;
import io.github.dafnipapado.university.repository.RoleRepository;
import io.github.dafnipapado.university.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class AdminUserCreator implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username}")
    private String adminUsername;
    @Value("${app.admin.password}")
    private String adminPassword;

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void run(String... args) throws EntityNotFoundException {
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new EntityNotFoundException("Role 'ADMIN' not found."));
        if (!userRepository.findByRoleId(adminRole.getId()).isPresent()) {
            log.info("No admin found in the database. Creating new admin...");
            User user = new User();
            user.setUsername(adminUsername);
            user.setPassword(passwordEncoder.encode(adminPassword));
            user.setRole(adminRole);
            userRepository.save(user);
            log.info("Admin was created successfully.");
        }
    }
}
