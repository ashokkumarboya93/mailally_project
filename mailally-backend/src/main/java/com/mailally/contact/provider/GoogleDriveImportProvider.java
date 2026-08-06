package com.mailally.contact.provider;

import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Component
public class GoogleDriveImportProvider implements ContactImportProvider {

    @Override
    public boolean supports(SourceType sourceType) {
        return sourceType == SourceType.DRIVE;
    }

    @Override
    public List<ContactRawRow> readRows(InputStream inputStream) throws Exception {
        return Collections.emptyList();
    }
}
