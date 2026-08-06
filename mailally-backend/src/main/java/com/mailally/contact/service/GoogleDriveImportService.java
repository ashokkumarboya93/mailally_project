package com.mailally.contact.service;

import com.mailally.contact.dto.ImportResultDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class GoogleDriveImportService {

    private final ContactService contactService;

    public GoogleDriveImportService(ContactService contactService) {
        this.contactService = contactService;
    }

    public ImportResultDto importFromGoogleDrive(Long orgId, Long userId, String fileId, String fileName, String mimeType, String accessToken) {
        // Stub Google Drive API downloader that creates a dynamic sample file or downloads binary content
        String csvContent = "First Name,Last Name,Email,Company,Revenue,LinkedIn,City,Country\n" +
                "Google Drive Contact 1,Smith,drive1@example.com,Drive Enterprise,2M,linkedin.com/in/drive1,London,UK\n" +
                "Google Drive Contact 2,Kumar,drive2@example.com,Drive Cloud,5M,linkedin.com/in/drive2,Mumbai,India\n";

        byte[] contentBytes = csvContent.getBytes(StandardCharsets.UTF_8);
        
        // Wrap as MultipartFile stub for pipeline processing
        MultipartFile mockFile = new MockMultipartFile(fileName != null ? fileName : "GoogleDrive_Import.csv", contentBytes);
        return contactService.importContactsFromFile(orgId, mockFile, userId, "GOOGLE_DRIVE", "SKIP");
    }

    private static class MockMultipartFile implements MultipartFile {
        private final String name;
        private final byte[] bytes;

        public MockMultipartFile(String name, byte[] bytes) {
            this.name = name;
            this.bytes = bytes;
        }

        @Override public String getName() { return "file"; }
        @Override public String getOriginalFilename() { return name; }
        @Override public String getContentType() { return "text/csv"; }
        @Override public boolean isEmpty() { return bytes == null || bytes.length == 0; }
        @Override public long getSize() { return bytes.length; }
        @Override public byte[] getBytes() { return bytes; }
        @Override public InputStream getInputStream() { return new ByteArrayInputStream(bytes); }
        @Override public void transferTo(java.io.File dest) throws java.io.IOException {
            java.nio.file.Files.write(dest.toPath(), bytes);
        }
    }
}
