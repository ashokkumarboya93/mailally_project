package com.mailally.contact.dto;

/**
 * Response metadata for export operations.
 */
public class ExportResponseDto {

    private String fileName;
    private String contentType;
    private long recordCount;
    private byte[] fileContent;

    public ExportResponseDto() {
    }

    public ExportResponseDto(String fileName, String contentType, long recordCount, byte[] fileContent) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.recordCount = recordCount;
        this.fileContent = fileContent;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(long recordCount) {
        this.recordCount = recordCount;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public static ExportResponseDtoBuilder builder() {
        return new ExportResponseDtoBuilder();
    }

    public static class ExportResponseDtoBuilder {
        private String fileName;
        private String contentType;
        private long recordCount;
        private byte[] fileContent;

        ExportResponseDtoBuilder() {
        }

        public ExportResponseDtoBuilder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public ExportResponseDtoBuilder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public ExportResponseDtoBuilder recordCount(long recordCount) {
            this.recordCount = recordCount;
            return this;
        }

        public ExportResponseDtoBuilder fileContent(byte[] fileContent) {
            this.fileContent = fileContent;
            return this;
        }

        public ExportResponseDto build() {
            return new ExportResponseDto(fileName, contentType, recordCount, fileContent);
        }
    }
}
