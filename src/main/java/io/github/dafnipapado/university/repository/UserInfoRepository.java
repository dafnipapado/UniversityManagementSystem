package io.github.dafnipapado.university.repository;

import io.github.dafnipapado.university.model.User;
import io.github.dafnipapado.university.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    Optional<UserInfo> findByUser(User user);
    Optional<UserInfo> findByAfm(String afm);
    Optional<UserInfo> findByEmail(String email);
}
