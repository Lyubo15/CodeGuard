package com.tu.codeguard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeAnalysisResultDTO {

    private String minifiedFileURL;

    private String analyzedFileURL;
}
