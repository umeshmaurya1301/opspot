package com.opspot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "hackathon_phases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HackathonPhase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false)
    private String phaseName;

    private LocalDate startDate;

    private LocalDate endDate;

    private String location;

    private Integer phaseOrder;
}
