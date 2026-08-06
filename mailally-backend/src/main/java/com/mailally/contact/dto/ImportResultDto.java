package com.mailally.contact.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Summary DTO returned after bulk CSV/Excel import execution.
 */
public class ImportResultDto {

    private int totalRows;
    private int importedCount;
    private int skippedCount;
    private int failedCount;
    private int invalidCount;
    private int duplicateCount;
    private long durationMs;
    private String message;
    private Long collectionId;
    private List<String> errorMessages = new ArrayList<>();

    public Long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }

    public ImportResultDto() {
    }

    public ImportResultDto(int totalRows, int importedCount, int skippedCount, int failedCount, int duplicateCount, List<String> errorMessages) {
        this.totalRows = totalRows;
        this.importedCount = importedCount;
        this.skippedCount = skippedCount;
        this.failedCount = failedCount;
        this.invalidCount = failedCount;
        this.duplicateCount = duplicateCount;
        this.errorMessages = errorMessages != null ? errorMessages : new ArrayList<>();
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getImportedCount() {
        return importedCount;
    }

    public void setImportedCount(int importedCount) {
        this.importedCount = importedCount;
    }

    public int getSuccessCount() {
        return importedCount;
    }

    public void setSuccessCount(int successCount) {
        this.importedCount = successCount;
    }

    public int getSkippedCount() {
        return skippedCount;
    }

    public void setSkippedCount(int skippedCount) {
        this.skippedCount = skippedCount;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
        this.invalidCount = failedCount;
    }

    public int getInvalidCount() {
        return invalidCount;
    }

    public void setInvalidCount(int invalidCount) {
        this.invalidCount = invalidCount;
        this.failedCount = invalidCount;
    }

    public int getDuplicateCount() {
        return duplicateCount;
    }

    public void setDuplicateCount(int duplicateCount) {
        this.duplicateCount = duplicateCount;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public static ImportResultDtoBuilder builder() {
        return new ImportResultDtoBuilder();
    }

    public static class ImportResultDtoBuilder {
        private int totalRows;
        private int importedCount;
        private int skippedCount;
        private int failedCount;
        private int duplicateCount;
        private long durationMs;
        private String message;
        private List<String> errorMessages = new ArrayList<>();

        ImportResultDtoBuilder() {
        }

        public ImportResultDtoBuilder totalRows(int totalRows) {
            this.totalRows = totalRows;
            return this;
        }

        public ImportResultDtoBuilder importedCount(int importedCount) {
            this.importedCount = importedCount;
            return this;
        }

        public ImportResultDtoBuilder successCount(int successCount) {
            this.importedCount = successCount;
            return this;
        }

        public ImportResultDtoBuilder skippedCount(int skippedCount) {
            this.skippedCount = skippedCount;
            return this;
        }

        public ImportResultDtoBuilder failedCount(int failedCount) {
            this.failedCount = failedCount;
            return this;
        }

        public ImportResultDtoBuilder invalidCount(int invalidCount) {
            this.failedCount = invalidCount;
            return this;
        }

        public ImportResultDtoBuilder duplicateCount(int duplicateCount) {
            this.duplicateCount = duplicateCount;
            return this;
        }

        public ImportResultDtoBuilder durationMs(long durationMs) {
            this.durationMs = durationMs;
            return this;
        }

        public ImportResultDtoBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ImportResultDtoBuilder errorMessages(List<String> errorMessages) {
            this.errorMessages = errorMessages;
            return this;
        }

        public ImportResultDto build() {
            ImportResultDto dto = new ImportResultDto(totalRows, importedCount, skippedCount, failedCount, duplicateCount, errorMessages);
            dto.setDurationMs(durationMs);
            dto.setMessage(message);
            return dto;
        }
    }
}
