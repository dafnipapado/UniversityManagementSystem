package io.github.dafnipapado.university.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "examinations")
public class Examination extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID uuid;

    @Column(nullable = false, columnDefinition = "DATE", name = "examination_date")
    private LocalDate examinationDate;

    @Column(columnDefinition = "TIME", name = "start_time")
    private LocalTime startTime;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false, name = "passing_grade")
    private Integer passingGrade;

    @OneToOne
    @JoinColumn(name = "course_offering_id")
    private CourseOffering courseOffering;

    @OneToMany(mappedBy = "examination")
    private Set<ExamResult> results;

    @PrePersist
    public void initializeUuid() {
        this.uuid = UUID.randomUUID();
    }

    public void addCourseOffering(CourseOffering courseOffering) {
        this.setCourseOffering(courseOffering);
        courseOffering.setExamination(this);
    }
}
