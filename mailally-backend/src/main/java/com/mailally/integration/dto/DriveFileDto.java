package com.mailally.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriveFileDto {

    private String id;
    private String name;
    private String mimeType;
    private String size;
    private String iconLink;
    private String webViewLink;
    private String modifiedTime;
    private boolean isSpreadsheet;
}
