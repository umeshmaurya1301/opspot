package com.opspot.entity;

import com.opspot.enums.ApplicationStatus;
import com.opspot.enums.WorkMode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "jobs", uniqueConstraints = @UniqueConstraint(columnNames = "job_link"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String company;

    private String location;

    @Enumerated(EnumType.STRING)
    private WorkMode workMode;

    // Comma-separated list of skills
    @Column(columnDefinition = "TEXT")
    private String skills;

    private Integer experienceMin;

    private Integer experienceMax;

    private String salaryRange;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "job_link", nullable = false, unique = true)
    private String jobLink;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ApplicationStatus status = ApplicationStatus.SAVED;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
