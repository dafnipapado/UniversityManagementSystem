package io.github.dafnipapado.university.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "examinations")
public class Examination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID uuid;

    @Column(nullable = false, columnDefinition = "DATE")
    private Date examination_date;

    @OneToMany(mappedBy = "examination")
    private Set<CourseOffering> offerings;

    @OneToMany(mappedBy = "examination")
    private Set<ExamResult> results;

    @PrePersist
    public void initializeUuid() {
        this.uuid = UUID.randomUUID();
    }
}
