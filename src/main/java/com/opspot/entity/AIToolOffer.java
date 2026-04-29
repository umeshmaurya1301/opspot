package com.opspot.entity;

import com.opspot.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_tool_offers", uniqueConstraints = @UniqueConstraint(columnNames = "offer_link"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIToolOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String toolName;

    @Column(nullable = false)
    private String offerTitle;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isFree = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean forProfessionals = false;

    private LocalDate validTill;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "offer_link", nullable = false, unique = true)
    private String offerLink;

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
