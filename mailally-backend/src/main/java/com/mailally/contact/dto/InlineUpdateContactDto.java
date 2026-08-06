package com.mailally.contact.dto;

import jakarta.validation.constraints.NotBlank;

public class InlineUpdateContactDto {

    @NotBlank(message = "Field name is required")
    private String fieldName;

    private String fieldValue;

    public InlineUpdateContactDto() {
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
