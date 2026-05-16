package com.myapp.university.model.static_data;

import com.myapp.university.model.UserInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @OneToMany(mappedBy = "region")
    private Set<UserInfo> userInfo;

    public void saveUserInfo(UserInfo info) {
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
