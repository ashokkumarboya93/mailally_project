package com.mailally.contact.provider;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ImportProviderFactory {

    private final List<ContactImportProvider> providers;

    public ImportProviderFactory(List<ContactImportProvider> providers) {
        this.providers = providers;
    }

    public ContactImportProvider getProvider(SourceType sourceType) {
        return providers.stream()
                .filter(p -> p.supports(sourceType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No import provider registered for source type: " + sourceType));
    }
}
