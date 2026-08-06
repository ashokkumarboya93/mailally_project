package com.mailally.contact.service;

import com.mailally.contact.dto.ImportProgressDto;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ImportProgressTracker {

    private final Map<String, ProgressState> trackerMap = new ConcurrentHashMap<>();

    public void init(String batchCode, int totalRows) {
        ProgressState state = new ProgressState();
        state.batchCode = batchCode;
        state.stage = "UPLOADING";
        state.totalRows = totalRows;
        state.startTimeMs = System.currentTimeMillis();
        trackerMap.put(batchCode, state);
    }

    public void update(String batchCode, String stage, int currentRow, int importedCount, int skippedCount, int invalidCount) {
        ProgressState state = trackerMap.get(batchCode);
        if (state != null) {
            state.stage = stage;
            state.currentRow = currentRow;
            state.importedCount = importedCount;
            state.skippedCount = skippedCount;
            state.invalidCount = invalidCount;
        }
    }

    public void complete(String batchCode) {
        ProgressState state = trackerMap.get(batchCode);
        if (state != null) {
            state.stage = "COMPLETED";
            state.currentRow = state.totalRows;
        }
    }

    public void fail(String batchCode) {
        ProgressState state = trackerMap.get(batchCode);
        if (state != null) {
            state.stage = "FAILED";
        }
    }

    public ImportProgressDto getProgress(String batchCode) {
        ProgressState state = trackerMap.get(batchCode);
        if (state == null) {
            ImportProgressDto dto = new ImportProgressDto();
            dto.setBatchCode(batchCode);
            dto.setStage("NOT_FOUND");
            dto.setPercentage(100);
            return dto;
        }

        ImportProgressDto dto = new ImportProgressDto();
        dto.setBatchCode(state.batchCode);
        dto.setStage(state.stage);
        dto.setCurrentRow(state.currentRow);
        dto.setTotalRows(state.totalRows);

        int remaining = Math.max(0, state.totalRows - state.currentRow);
        dto.setRowsRemaining(remaining);

        int pct = state.totalRows > 0 ? (int) Math.min(100, Math.round(((double) state.currentRow / state.totalRows) * 100)) : 0;
        dto.setPercentage(pct);

        long elapsedMs = System.currentTimeMillis() - state.startTimeMs;
        double elapsedSec = Math.max(0.1, elapsedMs / 1000.0);
        double speed = Math.round((state.currentRow / elapsedSec) * 10.0) / 10.0;
        dto.setSpeedRowsPerSec(speed);

        long eta = speed > 0 ? (long) Math.ceil(remaining / speed) : 0L;
        dto.setEtaSeconds(eta);

        dto.setImportedCount(state.importedCount);
        dto.setSkippedCount(state.skippedCount);
        dto.setInvalidCount(state.invalidCount);

        return dto;
    }

    private static class ProgressState {
        String batchCode;
        volatile String stage;
        volatile int currentRow;
        volatile int totalRows;
        volatile int importedCount;
        volatile int skippedCount;
        volatile int invalidCount;
        long startTimeMs;
    }
}
