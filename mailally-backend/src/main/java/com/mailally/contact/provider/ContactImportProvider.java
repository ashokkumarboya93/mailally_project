package com.mailally.contact.provider;

import java.io.InputStream;
import java.util.List;

public interface ContactImportProvider {

    boolean supports(SourceType sourceType);

    List<ContactRawRow> readRows(InputStream inputStream) throws Exception;
}
