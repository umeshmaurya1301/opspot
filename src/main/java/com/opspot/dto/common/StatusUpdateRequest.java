package com.opspot.dto.common;

import com.opspot.enums.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StatusUpdateRequest {

    @NotNull(message = "Status must not be null")
    private ApplicationStatus status;
}
