package io.github.dafnipapado.university.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "semesters")
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, updatable = false, unique = true, columnDefinition = "BINARY(16)")
    private UUID uuid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "YEAR")
    private Integer year;

    @Column(nullable = false, columnDefinition = "DATE", name = "starts_at")
    private LocalDate startsAt;

    @Column(nullable = false, columnDefinition = "DATE", name = "ends_at")
    private LocalDate endsAt;

    @Column(nullable = false, columnDefinition = "DATETIME", name = "registration_deadline")
    private LocalDateTime registrationDeadline;

    @Column(nullable = false, name = "is_active")
    private boolean active;

    @OneToMany(mappedBy = "semester")
    private Set<CourseOffering> offerings;

    @PrePersist
    public void initializeUuid() {
        this.uuid = UUID.randomUUID();
    }

    public void addCourseOffering(CourseOffering courseOffering) {
        offerings.add(courseOffering);
        courseOffering.setSemester(this);
    }

    public void removeCourseOffering(CourseOffering courseOffering) {
        offerings.remove(courseOffering);
    }
}
