package com.myapp.university.model.static_data;

import com.myapp.university.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "role")
    private Set<User> users;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private Set<Capability> capabilities;

    public Set<Capability> getAllCapabilities() {
        return Collections.unmodifiableSet(capabilities);
    }

    public void addUser(User user) {
        if (users.isEmpty()) users = new HashSet<>();
        users.add(user);
        user.setRole(this);
    }

    public void removeUser(User user) {
        if (users.isEmpty()) return;
        users.remove(user);
        user.setRole(null);
    }

    public void addUsers(Collection<User> users) {
        users.forEach(this::addUser);
    }

    public Set<User> getAllUsers() {
        return Collections.unmodifiableSet(users);
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Role role)) return false;
        return getId().equals(role.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
