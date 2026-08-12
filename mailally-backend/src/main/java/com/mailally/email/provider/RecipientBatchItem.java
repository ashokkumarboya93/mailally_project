package com.mailally.email.provider;

import java.util.Map;

/**
 * Encapsulates single recipient payload for provider batch requests.
 */
public class RecipientBatchItem {

    private final Long recipientLogId;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final Map<String, String> params;
    private final String personalizedSubject;
    private final String personalizedHtml;

    public RecipientBatchItem(Long recipientLogId, String email, String firstName, String lastName,
                              Map<String, String> params, String personalizedSubject, String personalizedHtml) {
        this.recipientLogId = recipientLogId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.params = params;
        this.personalizedSubject = personalizedSubject;
        this.personalizedHtml = personalizedHtml;
    }

    public Long getRecipientLogId() { return recipientLogId; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public Map<String, String> getParams() { return params; }
    public String getPersonalizedSubject() { return personalizedSubject; }
    public String getPersonalizedHtml() { return personalizedHtml; }
}
