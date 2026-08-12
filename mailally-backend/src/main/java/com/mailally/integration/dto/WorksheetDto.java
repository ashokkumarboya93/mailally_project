package com.mailally.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorksheetDto {

    private Integer sheetId;
    private String title;
    private Integer index;
    private Integer rowCount;
    private Integer columnCount;
}
