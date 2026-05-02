package com.opspot.entity;

import com.opspot.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "marathons", uniqueConstraints = @UniqueConstraint(columnNames = "registration_link"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Marathon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String organizer;

    private LocalDate date;

    private LocalDate registrationDeadline;

    private Double entryFee;

    @Column(nullable = false)
    private boolean isFree;

    private String location;

    @ElementCollection
    @CollectionTable(name = "marathon_distance_categories", joinColumns = @JoinColumn(name = "marathon_id"))
    @Column(name = "distance_category")
    @Builder.Default
    private List<String> distanceCategories = new ArrayList<>();

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
