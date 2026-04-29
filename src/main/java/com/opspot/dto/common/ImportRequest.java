package com.opspot.dto.common;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImportRequest {

    @NotBlank(message = "JSON payload must not be blank")
    private String json;
}
