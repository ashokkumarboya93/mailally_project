package com.mailally.template.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mailally.contact.entity.Contact;
import com.mailally.contact.entity.DynamicFieldRegistry;
import com.mailally.contact.repository.DynamicFieldRegistryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TemplateVariableEngine {

    private final DynamicFieldRegistryRepository fieldRegistryRepository;
    private final ObjectMapper objectMapper;
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{([^\\}]+)\\}\\}");

    public TemplateVariableEngine(DynamicFieldRegistryRepository fieldRegistryRepository, ObjectMapper objectMapper) {
        this.fieldRegistryRepository = fieldRegistryRepository;
        this.objectMapper = objectMapper;
    }

    public List<String> getAvailableVariablesForOrg(Long orgId) {
        Set<String> variables = new LinkedHashSet<>();
        
        // Standard fields
        variables.add("firstName");
        variables.add("lastName");
        variables.add("email");
        variables.add("phone");
        variables.add("company");
        variables.add("department");
        variables.add("designation");
        variables.add("city");
        variables.add("state");
        variables.add("country");
        
        // Dynamic custom fields from registry
        List<DynamicFieldRegistry> dynamicFields = fieldRegistryRepository.findByOrganizationIdOrderByOrderIndexAscCreatedAtAsc(orgId);
        for (DynamicFieldRegistry field : dynamicFields) {
            variables.add(field.getFieldKey());
        }

        // System variables
        variables.add("currentDate");
        variables.add("currentTime");
        variables.add("campaignName");
        variables.add("unsubscribeLink");
        variables.add("viewInBrowser");
        variables.add("organizationName");

        return new ArrayList<>(variables);
    }

    public String renderTemplate(String templateText, Contact contact, String campaignName, String orgName) {
        if (templateText == null || templateText.isEmpty() || contact == null) {
            return templateText;
        }

        Map<String, String> values = new HashMap<>();
        values.put("firstName", contact.getFirstName() != null ? contact.getFirstName() : "");
        values.put("lastName", contact.getLastName() != null ? contact.getLastName() : "");
        values.put("email", contact.getEmail() != null ? contact.getEmail() : "");
        values.put("phone", contact.getPhone() != null ? contact.getPhone() : "");
        values.put("company", contact.getCompany() != null ? contact.getCompany() : "");
        values.put("department", contact.getDepartment() != null ? contact.getDepartment() : "");
        values.put("designation", contact.getDesignation() != null ? contact.getDesignation() : "");
        values.put("city", contact.getCity() != null ? contact.getCity() : "");
        values.put("state", contact.getState() != null ? contact.getState() : "");
        values.put("country", contact.getCountry() != null ? contact.getCountry() : "");

        // Custom fields JSON
        if (contact.getCustomFields() != null && !contact.getCustomFields().isEmpty()) {
            try {
                Map<String, Object> customMap = objectMapper.readValue(contact.getCustomFields(), new TypeReference<Map<String, Object>>() {});
                for (Map.Entry<String, Object> entry : customMap.entrySet()) {
                    if (entry.getValue() != null) {
                        values.put(entry.getKey(), entry.getValue().toString());
                    }
                }
            } catch (Exception e) {
                // Ignore parsing errors gracefully
            }
        }

        // System tags
        values.put("currentDate", LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        values.put("currentTime", LocalDate.now().toString());
        values.put("campaignName", campaignName != null ? campaignName : "Campaign");
        values.put("organizationName", orgName != null ? orgName : "MailAlly Enterprise");
        values.put("unsubscribeLink", "#unsubscribe");
        values.put("viewInBrowser", "#view-in-browser");

        Matcher matcher = VARIABLE_PATTERN.matcher(templateText);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String varName = matcher.group(1).trim();
            String replacement = values.getOrDefault(varName, matcher.group(0));
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(sb);

        return sb.toString();
    }
}
