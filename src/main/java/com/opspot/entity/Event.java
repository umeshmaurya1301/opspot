package com.opspot.entity;

import com.opspot.enums.ApplicationStatus;
import com.opspot.enums.EventType;
import com.opspot.enums.WorkMode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "events", uniqueConstraints = @UniqueConstraint(columnNames = "registration_link"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String organizer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType eventType;

    @Enumerated(EnumType.STRING)
    private WorkMode workMode;

    private String city;

    private String theme;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate registrationDeadline;

    @Column(nullable = false)
    private boolean professionalAllowed;

    @Column(name = "registration_link", nullable = false, unique = true)
    private String registrationLink;

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
