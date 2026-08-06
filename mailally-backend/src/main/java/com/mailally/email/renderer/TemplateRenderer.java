package com.mailally.email.renderer;

import com.mailally.contact.entity.Contact;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility renderer component replacing dynamic variable placeholders (e.g., {{firstName}}, {{lastName}}, {{company}})
 * with actual contact data.
 */
@Component
public class TemplateRenderer {

    /**
     * Personalizes template string using values from {@link Contact}.
     */
    public String render(String template, Contact contact) {
        if (template == null || template.isBlank()) {
            return "";
        }
        if (contact == null) {
            return template;
        }

        Map<String, String> variables = buildVariableMap(contact);
        String result = template;

        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            String replacement = entry.getValue() != null ? entry.getValue() : "";
            result = result.replace(placeholder, replacement);
        }

        return result;
    }

    private Map<String, String> buildVariableMap(Contact contact) {
        Map<String, String> vars = new HashMap<>();
        vars.put("firstName", contact.getFirstName());
        vars.put("lastName", contact.getLastName());
        vars.put("email", contact.getEmail());
        vars.put("phone", contact.getPhone());
        vars.put("company", contact.getCompany());
        vars.put("department", contact.getDepartment());
        vars.put("designation", contact.getDesignation());
        vars.put("city", contact.getCity());
        vars.put("state", contact.getState());
        vars.put("country", contact.getCountry());
        vars.put("address", contact.getAddress());
        vars.put("postalCode", contact.getPostalCode());
        vars.put("website", contact.getWebsite());
        return vars;
    }
}
