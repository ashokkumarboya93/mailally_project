package com.mailally.contact.provider;

import com.mailally.contact.pipeline.ColumnAutoMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ExcelImportProvider implements ContactImportProvider {

    @Override
    public boolean supports(SourceType sourceType) {
        return sourceType == SourceType.EXCEL;
    }

    @Override
    public List<ContactRawRow> readRows(InputStream inputStream) throws Exception {
        List<ContactRawRow> rows = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet.getPhysicalNumberOfRows() == 0) return rows;

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) return rows;

            Map<Integer, String> headers = new HashMap<>();
            for (Cell cell : headerRow) {
                headers.put(cell.getColumnIndex(), getCellValueAsString(cell));
            }

            DataFormatter formatter = new DataFormatter();
            int rowNumber = 1;

            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;
                rowNumber++;

                Map<String, String> rowValues = new HashMap<>();
                boolean emptyRow = true;

                for (Map.Entry<Integer, String> h : headers.entrySet()) {
                    Cell cell = row.getCell(h.getKey());
                    String val = cell != null ? formatter.formatCellValue(cell).trim() : "";
                    if (!val.isEmpty()) emptyRow = false;
                    rowValues.put(h.getValue(), val);
                }

                if (!emptyRow) {
                    rows.add(ColumnAutoMapper.mapRow(rowValues, rowNumber));
                }
            }
        }
        return rows;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        return cell.getStringCellValue().trim();
    }
}
