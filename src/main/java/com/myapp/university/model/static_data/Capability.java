package com.myapp.university.model.static_data;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "capabilities")
public class Capability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;

    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.NONE)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
    name = "roles_capabilities",
    joinColumns = @JoinColumn(name = "capability_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public Set<Role> getAllRoles() {
        return Collections.unmodifiableSet(roles);
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Capability capability)) return false;

        return Objects.equals(getId(), capability.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
