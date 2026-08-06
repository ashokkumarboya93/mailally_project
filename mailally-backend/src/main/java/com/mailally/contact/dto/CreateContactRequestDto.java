package com.mailally.contact.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO payload for creating a new Contact.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateContactRequestDto {

    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @NotBlank(message = "Email address is required")
    @Email(message = "Invalid email address format")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

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

    public CreateContactRequestDto() {
    }

    public CreateContactRequestDto(String firstName, String lastName, String email, String phone, String company,
                                 String department, String designation, String city, String state, String country,
                                 String address, String postalCode, String website, String tags, String notes, String status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public static CreateContactRequestDtoBuilder builder() {
        return new CreateContactRequestDtoBuilder();
    }

    public static class CreateContactRequestDtoBuilder {
        private String firstName;
        private String lastName;
        private String email;
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

        CreateContactRequestDtoBuilder() {
        }

        public CreateContactRequestDtoBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public CreateContactRequestDtoBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public CreateContactRequestDtoBuilder email(String email) {
            this.email = email;
            return this;
        }

        public CreateContactRequestDtoBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public CreateContactRequestDtoBuilder company(String company) {
            this.company = company;
            return this;
        }

        public CreateContactRequestDtoBuilder department(String department) {
            this.department = department;
            return this;
        }

        public CreateContactRequestDtoBuilder designation(String designation) {
            this.designation = designation;
            return this;
        }

        public CreateContactRequestDtoBuilder city(String city) {
            this.city = city;
            return this;
        }

        public CreateContactRequestDtoBuilder state(String state) {
            this.state = state;
            return this;
        }

        public CreateContactRequestDtoBuilder country(String country) {
            this.country = country;
            return this;
        }

        public CreateContactRequestDtoBuilder address(String address) {
            this.address = address;
            return this;
        }

        public CreateContactRequestDtoBuilder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public CreateContactRequestDtoBuilder website(String website) {
            this.website = website;
            return this;
        }

        public CreateContactRequestDtoBuilder tags(String tags) {
            this.tags = tags;
            return this;
        }

        public CreateContactRequestDtoBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public CreateContactRequestDtoBuilder status(String status) {
            this.status = status;
            return this;
        }

        public CreateContactRequestDto build() {
            return new CreateContactRequestDto(firstName, lastName, email, phone, company, department, designation,
                    city, state, country, address, postalCode, website, tags, notes, status);
        }
    }
}
