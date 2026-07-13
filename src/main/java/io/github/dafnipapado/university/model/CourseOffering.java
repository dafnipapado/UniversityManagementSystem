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
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "course_offerings")
public class CourseOffering {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @OneToMany(mappedBy = "courseOffering")
    private Set<Examination> examinations;

    @OneToMany(mappedBy = "offering")
    private Set<Enrollment> enrollments;

    @PrePersist
    public void initializeUuid() {
        this.uuid = UUID.randomUUID();
    }
}
