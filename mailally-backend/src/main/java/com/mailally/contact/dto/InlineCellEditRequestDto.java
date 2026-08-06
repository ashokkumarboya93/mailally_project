package com.mailally.contact.dto;

public class InlineCellEditRequestDto {
    private String fieldName;
    private String newValue;
    private Boolean isCustomField = false;

    public InlineCellEditRequestDto() {}

    public String getFieldName() { return fieldName; }
    public void setFieldName(String fieldName) { this.fieldName = fieldName; }

    public String getNewValue() { return newValue; }
    public void setNewValue(String newValue) { this.newValue = newValue; }

    public Boolean getIsCustomField() { return isCustomField; }
    public void setIsCustomField(Boolean customField) { isCustomField = customField; }
}
