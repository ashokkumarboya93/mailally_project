package com.mailally.contact.provider;

import com.mailally.contact.pipeline.ColumnAutoMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CsvImportProvider implements ContactImportProvider {

    @Override
    public boolean supports(SourceType sourceType) {
        return sourceType == SourceType.CSV;
    }

    @Override
    public List<ContactRawRow> readRows(InputStream inputStream) throws Exception {
        List<ContactRawRow> rows = new ArrayList<>();
        try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).setIgnoreHeaderCase(true).setTrim(true).build())) {

            Map<String, Integer> headerMap = csvParser.getHeaderMap();
            int rowNumber = 1;

            for (CSVRecord record : csvParser) {
                rowNumber++;
                Map<String, String> rowValues = new HashMap<>();
                for (Map.Entry<String, Integer> entry : headerMap.entrySet()) {
                    String colName = entry.getKey();
                    String colVal = record.isMapped(colName) ? record.get(colName) : "";
                    rowValues.put(colName, colVal);
                }
                rows.add(ColumnAutoMapper.mapRow(rowValues, rowNumber));
            }
        }
        return rows;
    }
}
