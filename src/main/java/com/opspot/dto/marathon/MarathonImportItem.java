package com.opspot.dto.marathon;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MarathonImportItem {
    private String title;
    private String description;
    private String organizer;
    private LocalDate date;
    private LocalDate registrationDeadline;
    private Double entryFee;
    private Boolean isFree;
    private String location;
    private List<String> distanceCategories;
    private String registrationLink;
}
