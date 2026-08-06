package com.mailally.contact.dto;

public class ImportProgressDto {

    private String batchCode;
    private String stage; // UPLOADING, READING, VALIDATING, SAVING, COMPLETED, FAILED
    private int percentage;
    private int currentRow;
    private int totalRows;
    private int rowsRemaining;
    private double speedRowsPerSec;
    private long etaSeconds;
    private int importedCount;
    private int skippedCount;
    private int invalidCount;

    public ImportProgressDto() {
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getRowsRemaining() {
        return rowsRemaining;
    }

    public void setRowsRemaining(int rowsRemaining) {
        this.rowsRemaining = rowsRemaining;
    }

    public double getSpeedRowsPerSec() {
        return speedRowsPerSec;
    }

    public void setSpeedRowsPerSec(double speedRowsPerSec) {
        this.speedRowsPerSec = speedRowsPerSec;
    }

    public long getEtaSeconds() {
        return etaSeconds;
    }

    public void setEtaSeconds(long etaSeconds) {
        this.etaSeconds = etaSeconds;
    }

    public int getImportedCount() {
        return importedCount;
    }

    public void setImportedCount(int importedCount) {
        this.importedCount = importedCount;
    }

    public int getSkippedCount() {
        return skippedCount;
    }

    public void setSkippedCount(int skippedCount) {
        this.skippedCount = skippedCount;
    }

    public int getInvalidCount() {
        return invalidCount;
    }

    public void setInvalidCount(int invalidCount) {
        this.invalidCount = invalidCount;
    }
}
