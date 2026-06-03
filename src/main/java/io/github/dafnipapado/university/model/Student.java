package io.github.dafnipapado.university.model;

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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "students")
public class Student extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    private String studentAM;

    @Column(nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID uuid;

    @PrePersist
    public void initializeUuid() {
        this.uuid = UUID.randomUUID();
    }

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "student")
    private Set<ExamResult> results;

    @OneToMany(mappedBy = "student")
    private Set<Enrollment> enrollments;

    public void addUser(User user) {
        this.user = user;
        user.setStudent(this);
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Student student)) return false;
        return getId().equals(student.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
