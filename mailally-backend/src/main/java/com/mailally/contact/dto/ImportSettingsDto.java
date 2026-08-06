package com.mailally.contact.dto;

public class ImportSettingsDto {

    private String batchName;
    private String sourceType = "CSV"; // CSV, EXCEL, DRIVE
    private String duplicateStrategy = "SKIP"; // SKIP, REPLACE, MERGE, KEEP_LATEST, KEEP_EXISTING
    private Boolean validateEmail = true;
    private Boolean skipEmptyRows = true;
    private Boolean trimSpaces = true;
    private Boolean normalizePhone = true;

    public ImportSettingsDto() {
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getDuplicateStrategy() {
        return duplicateStrategy;
    }

    public void setDuplicateStrategy(String duplicateStrategy) {
        this.duplicateStrategy = duplicateStrategy;
    }

    public Boolean getValidateEmail() {
        return validateEmail;
    }

    public void setValidateEmail(Boolean validateEmail) {
        this.validateEmail = validateEmail;
    }

    public Boolean getSkipEmptyRows() {
        return skipEmptyRows;
    }

    public void setSkipEmptyRows(Boolean skipEmptyRows) {
        this.skipEmptyRows = skipEmptyRows;
    }

    public Boolean getTrimSpaces() {
        return trimSpaces;
    }

    public void setTrimSpaces(Boolean trimSpaces) {
        this.trimSpaces = trimSpaces;
    }

    public Boolean getNormalizePhone() {
        return normalizePhone;
    }

    public void setNormalizePhone(Boolean normalizePhone) {
        this.normalizePhone = normalizePhone;
    }
}
