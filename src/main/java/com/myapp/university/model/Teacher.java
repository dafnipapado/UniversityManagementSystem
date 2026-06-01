package com.myapp.university.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teachers")
public class Teacher extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    private String teacherAM;

    @Column(nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID uuid;

    @PrePersist
    public void initializeUuid() {
        this.uuid = UUID.randomUUID();
    }

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "teacher")
    private Set<CourseOffering> offerings;

    public void addUser(User user) {
        this.user = user;
        user.setTeacher(this);
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Teacher teacher)) return false;
        return getId().equals(teacher.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
