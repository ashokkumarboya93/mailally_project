package com.mailally.contact.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO payload for updating an existing Contact.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateContactRequestDto {

    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @Pattern(regexp = "^$|^[+]*[(]?[0-9]{1,4}[)]?[-\\s./0-9]*$", message = "Invalid phone number format")
    @Size(max = 30, message = "Phone number must not exceed 30 characters")
    private String phone;

    @Size(max = 150, message = "Company name must not exceed 150 characters")
    private String company;

    @Size(max = 100, message = "Department must not exceed 100 characters")
    private String department;

    @Size(max = 100, message = "Designation must not exceed 100 characters")
    private String designation;

    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @Size(max = 100, message = "State must not exceed 100 characters")
    private String state;

    @Size(max = 100, message = "Country must not exceed 100 characters")
    private String country;

    private String address;

    @Size(max = 20, message = "Postal code must not exceed 20 characters")
    private String postalCode;

    @Size(max = 255, message = "Website must not exceed 255 characters")
    private String website;

    @Size(max = 500, message = "Tags must not exceed 500 characters")
    private String tags;

    private String notes;

    @Size(max = 20, message = "Status must not exceed 20 characters")
    private String status;

    public UpdateContactRequestDto() {
    }

    public UpdateContactRequestDto(String firstName, String lastName, String phone, String company, String department,
                                 String designation, String city, String state, String country, String address,
                                 String postalCode, String website, String tags, String notes, String status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.company = company;
        this.department = department;
        this.designation = designation;
        this.city = city;
        this.state = state;
        this.country = country;
        this.address = address;
        this.postalCode = postalCode;
        this.website = website;
        this.tags = tags;
        this.notes = notes;
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static UpdateContactRequestDtoBuilder builder() {
        return new UpdateContactRequestDtoBuilder();
    }

    public static class UpdateContactRequestDtoBuilder {
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

        UpdateContactRequestDtoBuilder() {
        }

        public UpdateContactRequestDtoBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UpdateContactRequestDtoBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UpdateContactRequestDtoBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UpdateContactRequestDtoBuilder company(String company) {
            this.company = company;
            return this;
        }

        public UpdateContactRequestDtoBuilder department(String department) {
            this.department = department;
            return this;
        }

        public UpdateContactRequestDtoBuilder designation(String designation) {
            this.designation = designation;
            return this;
        }

        public UpdateContactRequestDtoBuilder city(String city) {
            this.city = city;
            return this;
        }

        public UpdateContactRequestDtoBuilder state(String state) {
            this.state = state;
            return this;
        }

        public UpdateContactRequestDtoBuilder country(String country) {
            this.country = country;
            return this;
        }

        public UpdateContactRequestDtoBuilder address(String address) {
            this.address = address;
            return this;
        }

        public UpdateContactRequestDtoBuilder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public UpdateContactRequestDtoBuilder website(String website) {
            this.website = website;
            return this;
        }

        public UpdateContactRequestDtoBuilder tags(String tags) {
            this.tags = tags;
            return this;
        }

        public UpdateContactRequestDtoBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public UpdateContactRequestDtoBuilder status(String status) {
            this.status = status;
            return this;
        }

        public UpdateContactRequestDto build() {
            return new UpdateContactRequestDto(firstName, lastName, phone, company, department, designation, city,
                    state, country, address, postalCode, website, tags, notes, status);
        }
    }
}
