package com.mailally.contact.provider;

import java.util.HashMap;
import java.util.Map;

public class ContactRawRow {

    private int rowNumber;
    private String rawEmail;
    private String firstName;
    private String lastName;
    private String phone;
    private String company;
    private String department;
    private String designation;
    private String city;
    private String state;
    private String country;
    private String address;
    private String postalCode;
    private String website;
    private String tags;
    private String notes;
    private String status;
    private Map<String, String> unmappedFields = new HashMap<>();

    public ContactRawRow() {
    }

    public int getRowNumber() { return rowNumber; }
    public void setRowNumber(int rowNumber) { this.rowNumber = rowNumber; }
    public String getRawEmail() { return rawEmail; }
    public void setRawEmail(String rawEmail) { this.rawEmail = rawEmail; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Map<String, String> getUnmappedFields() { return unmappedFields; }
    public void setUnmappedFields(Map<String, String> unmappedFields) { this.unmappedFields = unmappedFields; }
}
