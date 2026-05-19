package com.myapp.university.repository;

import com.myapp.university.model.User;
import com.myapp.university.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    Optional<UserInfo> findByUser(User user);
}
