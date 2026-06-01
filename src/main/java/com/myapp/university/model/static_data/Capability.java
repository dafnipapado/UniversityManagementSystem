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


}
