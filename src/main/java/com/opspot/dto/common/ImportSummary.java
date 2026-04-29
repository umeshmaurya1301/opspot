package com.opspot.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImportSummary {
    private final int total;
    private final int inserted;
    private final int skipped;
}
