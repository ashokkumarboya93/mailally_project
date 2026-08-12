package com.mailally.integration.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.mailally.exception.CustomException;
import com.mailally.integration.dto.DriveFileDto;
import com.mailally.integration.entity.GoogleIntegration;
import com.mailally.integration.repository.GoogleIntegrationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GoogleDriveService {

    private static final Logger log = LoggerFactory.getLogger(GoogleDriveService.class);
    private static final String APPLICATION_NAME = "MailAlly Enterprise";

    private final GoogleIntegrationRepository integrationRepository;
    private final GoogleOAuthService oAuthService;

    public GoogleDriveService(GoogleIntegrationRepository integrationRepository, GoogleOAuthService oAuthService) {
        this.integrationRepository = integrationRepository;
        this.oAuthService = oAuthService;
    }

    private Drive buildDriveClient(Long orgId) {
        GoogleIntegration integration = integrationRepository.findByOrganizationIdAndProvider(orgId, "GOOGLE")
                .orElseThrow(() -> new CustomException("No Google integration connected for this organization."));

        String validAccessToken = oAuthService.getValidAccessToken(integration);

        try {
            AccessToken token = new AccessToken(validAccessToken, new Date(System.currentTimeMillis() + 3600 * 1000L));
            GoogleCredentials credentials = GoogleCredentials.create(token);

            return new Drive.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(credentials))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (Exception e) {
            log.error("Failed to build Google Drive API client for orgId {}: {}", orgId, e.getMessage());
            throw new CustomException("Failed to connect to Google Drive API: " + e.getMessage());
        }
    }

    public List<DriveFileDto> listSupportedFiles(Long orgId, String searchQuery) {
        Drive drive = buildDriveClient(orgId);
        List<DriveFileDto> fileDtos = new ArrayList<>();

        try {
            FileList result = drive.files().list()
                    .setQ("trashed = false")
                    .setPageSize(100)
                    .setFields("nextPageToken, files(id, name, mimeType, size, iconLink, webViewLink, modifiedTime)")
                    .setOrderBy("modifiedTime desc")
                    .execute();

            List<File> files = result.getFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.getName() == null) continue;
                    String nameLower = f.getName().toLowerCase();
                    String mimeLower = f.getMimeType() != null ? f.getMimeType().toLowerCase() : "";

                    boolean isSheet = "application/vnd.google-apps.spreadsheet".equals(f.getMimeType());
                    boolean isSupported = isSheet ||
                            nameLower.endsWith(".csv") ||
                            nameLower.endsWith(".xlsx") ||
                            nameLower.endsWith(".xls") ||
                            nameLower.endsWith(".ods") ||
                            mimeLower.contains("csv") ||
                            mimeLower.contains("excel") ||
                            mimeLower.contains("spreadsheet");

                    if (!isSupported) continue;

                    if (searchQuery != null && !searchQuery.isBlank()) {
                        if (!nameLower.contains(searchQuery.trim().toLowerCase())) {
                            continue;
                        }
                    }

                    fileDtos.add(DriveFileDto.builder()
                            .id(f.getId())
                            .name(f.getName())
                            .mimeType(f.getMimeType())
                            .size(f.getSize() != null ? String.valueOf(f.getSize()) : (isSheet ? "Google Sheet" : "-"))
                            .iconLink(f.getIconLink())
                            .webViewLink(f.getWebViewLink())
                            .modifiedTime(f.getModifiedTime() != null ? f.getModifiedTime().toString() : null)
                            .isSpreadsheet(isSheet)
                            .build());
                }
            }
        } catch (Exception e) {
            log.error("Error listing files from Google Drive for orgId {}: {}", orgId, e.getMessage());
            throw new CustomException("Failed to retrieve Google Drive files: " + e.getMessage());
        }

        return fileDtos;
    }

    public DriveFileStreamResult getFileStream(Long orgId, String fileId) {
        Drive drive = buildDriveClient(orgId);

        try {
            File meta = drive.files().get(fileId).setFields("id, name, mimeType").execute();
            InputStream inputStream;

            if ("application/vnd.google-apps.spreadsheet".equals(meta.getMimeType())) {
                // Export Google Spreadsheet as CSV stream
                inputStream = drive.files().export(fileId, "text/csv").executeMediaAsInputStream();
            } else {
                // Download raw file content (CSV, XLSX, XLS)
                inputStream = drive.files().get(fileId).executeMediaAsInputStream();
            }

            return new DriveFileStreamResult(meta.getName(), meta.getMimeType(), inputStream);

        } catch (Exception e) {
            log.error("Error downloading file {} from Google Drive for orgId {}: {}", fileId, orgId, e.getMessage());
            throw new CustomException("Failed to download file from Google Drive: " + e.getMessage());
        }
    }

    public static class DriveFileStreamResult {
        private final String fileName;
        private final String mimeType;
        private final InputStream inputStream;

        public DriveFileStreamResult(String fileName, String mimeType, InputStream inputStream) {
            this.fileName = fileName;
            this.mimeType = mimeType;
            this.inputStream = inputStream;
        }

        public String getFileName() {
            return fileName;
        }

        public String getMimeType() {
            return mimeType;
        }

        public InputStream getInputStream() {
            return inputStream;
        }
    }
}
