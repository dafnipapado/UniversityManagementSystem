package io.github.dafnipapado.university.model.static_data;

import io.github.dafnipapado.university.model.UserInfo;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "regions")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    private String name;

    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "region")
    private Set<UserInfo> userInfo;

    public Set<UserInfo> getAllUserInfos() {
        return Collections.unmodifiableSet(userInfo);
    }

    public void addUserInfo(UserInfo info) {
        if (userInfo.isEmpty()) userInfo = new HashSet<>();
        userInfo.add(info);
        info.setRegion(this);
    }

    public void removeUserInfo(UserInfo info) {
        if (userInfo.isEmpty()) return;
        userInfo.remove(info);
        info.setRegion(null);
    }
}
