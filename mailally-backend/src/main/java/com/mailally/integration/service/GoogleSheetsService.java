package com.mailally.integration.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.mailally.exception.CustomException;
import com.mailally.integration.dto.WorksheetDto;
import com.mailally.integration.entity.GoogleIntegration;
import com.mailally.integration.repository.GoogleIntegrationRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GoogleSheetsService {

    private static final Logger log = LoggerFactory.getLogger(GoogleSheetsService.class);
    private static final String APPLICATION_NAME = "MailAlly Enterprise";

    private final GoogleIntegrationRepository integrationRepository;
    private final GoogleOAuthService oAuthService;

    public GoogleSheetsService(GoogleIntegrationRepository integrationRepository, GoogleOAuthService oAuthService) {
        this.integrationRepository = integrationRepository;
        this.oAuthService = oAuthService;
    }

    private Sheets buildSheetsClient(Long orgId) {
        GoogleIntegration integration = integrationRepository.findByOrganizationIdAndProvider(orgId, "GOOGLE")
                .orElseThrow(() -> new CustomException("No Google integration connected for this organization."));

        String validAccessToken = oAuthService.getValidAccessToken(integration);

        try {
            AccessToken token = new AccessToken(validAccessToken, new Date(System.currentTimeMillis() + 3600 * 1000L));
            GoogleCredentials credentials = GoogleCredentials.create(token);

            return new Sheets.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(credentials))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (Exception e) {
            log.error("Failed to build Google Sheets API client for orgId {}: {}", orgId, e.getMessage());
            throw new CustomException("Failed to connect to Google Sheets API: " + e.getMessage());
        }
    }

    public List<WorksheetDto> listWorksheets(Long orgId, String spreadsheetId) {
        Sheets sheets = buildSheetsClient(orgId);
        List<WorksheetDto> worksheets = new ArrayList<>();

        try {
            Spreadsheet spreadsheet = sheets.spreadsheets().get(spreadsheetId).execute();
            List<Sheet> sheetList = spreadsheet.getSheets();

            if (sheetList != null) {
                for (int i = 0; i < sheetList.size(); i++) {
                    SheetProperties props = sheetList.get(i).getProperties();
                    worksheets.add(WorksheetDto.builder()
                            .sheetId(props.getSheetId())
                            .title(props.getTitle())
                            .index(i)
                            .rowCount(props.getGridProperties() != null ? props.getGridProperties().getRowCount() : 0)
                            .columnCount(props.getGridProperties() != null ? props.getGridProperties().getColumnCount() : 0)
                            .build());
                }
            }
        } catch (Exception e) {
            log.error("Error retrieving worksheets for spreadsheet {} orgId {}: {}", spreadsheetId, orgId, e.getMessage());
            throw new CustomException("Failed to retrieve Google Sheets worksheets: " + e.getMessage());
        }

        return worksheets;
    }

    public InputStream fetchWorksheetCsvStream(Long orgId, String spreadsheetId, String worksheetTitle) {
        Sheets sheets = buildSheetsClient(orgId);

        try {
            String range = (worksheetTitle != null && !worksheetTitle.isBlank()) ? "'" + worksheetTitle.replace("'", "''") + "'" : "A1:ZZ50000";
            ValueRange response = sheets.spreadsheets().values().get(spreadsheetId, range).execute();
            List<List<Object>> values = response.getValues();

            if (values == null || values.isEmpty()) {
                throw new CustomException("Selected worksheet contains no data");
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try (CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out, StandardCharsets.UTF_8), CSVFormat.DEFAULT)) {
                for (List<Object> row : values) {
                    printer.printRecord(row);
                }
                printer.flush();
            }

            return new ByteArrayInputStream(out.toByteArray());

        } catch (CustomException ce) {
            throw ce;
        } catch (Exception e) {
            log.error("Error reading data from worksheet {} spreadsheet {} orgId {}: {}", worksheetTitle, spreadsheetId, orgId, e.getMessage());
            throw new CustomException("Failed to read Google Sheet data: " + e.getMessage());
        }
    }
}
