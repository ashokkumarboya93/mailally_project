package com.mailally.integration.controller;

import com.mailally.common.response.ApiResponse;
import com.mailally.contact.dto.ImportResultDto;
import com.mailally.contact.dto.ImportSettingsDto;
import com.mailally.contact.service.ContactService;
import com.mailally.integration.dto.DriveFileDto;
import com.mailally.integration.dto.GoogleIntegrationStatusDto;
import com.mailally.integration.dto.WorksheetDto;
import com.mailally.integration.entity.GoogleIntegration;
import com.mailally.integration.service.GoogleDriveService;
import com.mailally.integration.service.GoogleOAuthService;
import com.mailally.integration.service.GoogleSheetsService;
import com.mailally.security.CustomUserDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class GoogleIntegrationController {

    private final GoogleOAuthService oAuthService;
    private final GoogleDriveService driveService;
    private final GoogleSheetsService sheetsService;
    private final ContactService contactService;

    public GoogleIntegrationController(
            GoogleOAuthService oAuthService,
            GoogleDriveService driveService,
            GoogleSheetsService sheetsService,
            ContactService contactService) {
        this.oAuthService = oAuthService;
        this.driveService = driveService;
        this.sheetsService = sheetsService;
        this.contactService = contactService;
    }

    private <T> ApiResponse<T> createSuccessResponse(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message("Success")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private ApiResponse<String> createMessageResponse(String message) {
        return ApiResponse.<String>builder()
                .success(true)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @GetMapping("/api/v1/integrations/google/connect")
    public ResponseEntity<ApiResponse<Map<String, String>>> connectGoogle(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String authUrl = oAuthService.generateAuthorizationUrl(userDetails.getOrganizationId(), userDetails.getUserId());
        return ResponseEntity.ok(createSuccessResponse(Map.of("authorizationUrl", authUrl)));
    }

    @GetMapping("/api/integrations/google/callback")
    public ResponseEntity<String> handleCallback(
            @RequestParam("code") String code,
            @RequestParam("state") String state) {
        GoogleIntegration integration = oAuthService.handleCallback(code, state);

        String htmlResponse = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Google Authorization Successful</title>
                    <style>
                        body { font-family: 'Segoe UI', Arial, sans-serif; display: flex; align-items: center; justify-content: center; height: 100vh; margin: 0; background-color: #f8f9fa; }
                        .card { background: white; padding: 40px; border-radius: 16px; box-shadow: 0 10px 25px rgba(0,0,0,0.08); text-align: center; max-width: 400px; }
                        h2 { color: #16a34a; margin-top: 0; }
                        p { color: #4b5563; font-size: 14px; }
                    </style>
                </head>
                <body>
                    <div class="card">
                        <h2>✓ Google Connected</h2>
                        <p>Your Google Account (<b>""" + integration.getAccountEmail() + """
                        </b>) was connected successfully.</p>
                        <p>Closing window and returning to MailAlly...</p>
                    </div>
                    <script>
                        if (window.opener) {
                            window.opener.postMessage({ type: 'GOOGLE_AUTH_SUCCESS', email: '""" + integration.getAccountEmail() + """
                            ' }, '*');
                            setTimeout(function() { window.close(); }, 1500);
                        } else {
                            setTimeout(function() { window.location.href = 'http://localhost:5173/contacts'; }, 2000);
                        }
                    </script>
                </body>
                </html>
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity<>(htmlResponse, headers, HttpStatus.OK);
    }

    @GetMapping("/api/v1/integrations/google/status")
    public ResponseEntity<ApiResponse<GoogleIntegrationStatusDto>> getStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        GoogleIntegrationStatusDto status = oAuthService.getStatus(userDetails.getOrganizationId());
        return ResponseEntity.ok(createSuccessResponse(status));
    }

    @PostMapping("/api/v1/integrations/google/disconnect")
    public ResponseEntity<ApiResponse<String>> disconnectGoogle(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        oAuthService.disconnect(userDetails.getOrganizationId(), userDetails.getUserId());
        return ResponseEntity.ok(createMessageResponse("Disconnected Google integration successfully"));
    }

    @GetMapping("/api/v1/integrations/google/drive/files")
    public ResponseEntity<ApiResponse<List<DriveFileDto>>> listDriveFiles(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "search", required = false) String search) {
        List<DriveFileDto> files = driveService.listSupportedFiles(userDetails.getOrganizationId(), search);
        return ResponseEntity.ok(createSuccessResponse(files));
    }

    @GetMapping("/api/v1/integrations/google/sheets/{spreadsheetId}/worksheets")
    public ResponseEntity<ApiResponse<List<WorksheetDto>>> listWorksheets(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("spreadsheetId") String spreadsheetId) {
        List<WorksheetDto> worksheets = sheetsService.listWorksheets(userDetails.getOrganizationId(), spreadsheetId);
        return ResponseEntity.ok(createSuccessResponse(worksheets));
    }

    @PostMapping("/api/v1/integrations/google/import/drive")
    public ResponseEntity<ApiResponse<ImportResultDto>> importDriveFile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("fileId") String fileId,
            @RequestParam(value = "tag", required = false) String tag) throws IOException {

        GoogleDriveService.DriveFileStreamResult streamResult = driveService.getFileStream(userDetails.getOrganizationId(), fileId);

        byte[] bytes = streamResult.getInputStream().readAllBytes();
        MultipartFile multipartFile = new ByteArrayMultipartFile(
                bytes,
                streamResult.getFileName(),
                streamResult.getMimeType()
        );

        ImportSettingsDto settings = new ImportSettingsDto();
        settings.setBatchName(streamResult.getFileName());
        settings.setSourceType(streamResult.getFileName().toLowerCase().endsWith(".xlsx") ? "EXCEL" : "CSV");
        settings.setTag(tag != null && !tag.isBlank() ? tag : "Drive");

        ImportResultDto result = contactService.startImport(userDetails, multipartFile, settings);
        return ResponseEntity.ok(createSuccessResponse(result));
    }

    @PostMapping("/api/v1/integrations/google/import/sheet")
    public ResponseEntity<ApiResponse<ImportResultDto>> importGoogleSheet(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("spreadsheetId") String spreadsheetId,
            @RequestParam("worksheetName") String worksheetName,
            @RequestParam(value = "tag", required = false) String tag) throws IOException {

        InputStream csvStream = sheetsService.fetchWorksheetCsvStream(userDetails.getOrganizationId(), spreadsheetId, worksheetName);
        byte[] bytes = csvStream.readAllBytes();

        String fileName = worksheetName + ".csv";
        MultipartFile multipartFile = new ByteArrayMultipartFile(bytes, fileName, "text/csv");

        ImportSettingsDto settings = new ImportSettingsDto();
        settings.setBatchName(worksheetName);
        settings.setSourceType("CSV");
        settings.setTag(tag != null && !tag.isBlank() ? tag : "Sheets");

        ImportResultDto result = contactService.startImport(userDetails, multipartFile, settings);
        return ResponseEntity.ok(createSuccessResponse(result));
    }

    private static class ByteArrayMultipartFile implements MultipartFile {
        private final byte[] content;
        private final String name;
        private final String contentType;

        public ByteArrayMultipartFile(byte[] content, String name, String contentType) {
            this.content = content;
            this.name = name;
            this.contentType = contentType;
        }

        @Override public String getName() { return name; }
        @Override public String getOriginalFilename() { return name; }
        @Override public String getContentType() { return contentType; }
        @Override public boolean isEmpty() { return content == null || content.length == 0; }
        @Override public long getSize() { return content.length; }
        @Override public byte[] getBytes() { return content; }
        @Override public InputStream getInputStream() { return new ByteArrayInputStream(content); }
        @Override public void transferTo(File dest) throws IOException { throw new UnsupportedOperationException(); }
    }
}
