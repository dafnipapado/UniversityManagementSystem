package io.github.dafnipapado.university.model;

import io.github.dafnipapado.university.model.static_data.Department;
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
@Table(name = "courses")
public class Course extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID uuid;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    private int ects;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @OneToMany (mappedBy = "course")
    private Set<CourseOffering> offerings;

    @PrePersist
    public void initializeUuid() {
        this.uuid = UUID.randomUUID();
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Course course)) return false;
        return getUuid().equals(course.getUuid());
    }

    @Override
    public int hashCode() {
        return getUuid().hashCode();
    }
}
