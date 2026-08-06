package com.mailally.contact.mapper;

import com.mailally.contact.dto.CreateContactRequestDto;
import com.mailally.contact.dto.ContactResponseDto;
import com.mailally.contact.dto.UpdateContactRequestDto;
import com.mailally.contact.entity.Contact;
import com.mailally.organization.entity.Organization;
import org.springframework.stereotype.Component;

/**
 * Mapper component providing manual mapping between Contact entities and DTOs.
 */
@Component
public class ContactMapper {

    public Contact toContactEntity(CreateContactRequestDto dto, Organization organization, Long createdByUserId) {
        if (dto == null) {
            return null;
        }
        return Contact.builder()
                .organization(organization)
                .firstName(trimOrNull(dto.getFirstName()))
                .lastName(trimOrNull(dto.getLastName()))
                .email(dto.getEmail() != null ? dto.getEmail().trim().toLowerCase() : null)
                .phone(trimOrNull(dto.getPhone()))
                .company(trimOrNull(dto.getCompany()))
                .department(trimOrNull(dto.getDepartment()))
                .designation(trimOrNull(dto.getDesignation()))
                .city(trimOrNull(dto.getCity()))
                .state(trimOrNull(dto.getState()))
                .country(trimOrNull(dto.getCountry()))
                .address(trimOrNull(dto.getAddress()))
                .postalCode(trimOrNull(dto.getPostalCode()))
                .website(trimOrNull(dto.getWebsite()))
                .tags(trimOrNull(dto.getTags()))
                .notes(trimOrNull(dto.getNotes()))
                .status(dto.getStatus() != null && !dto.getStatus().isBlank() ? dto.getStatus().trim().toUpperCase() : "SUBSCRIBED")
                .createdBy(createdByUserId)
                .updatedBy(createdByUserId)
                .isDeleted(false)
                .build();
    }

    public ContactResponseDto toContactResponseDto(Contact contact) {
        if (contact == null) {
            return null;
        }
        ContactResponseDto dto = ContactResponseDto.builder()
                .id(contact.getId())
                .organizationId(contact.getOrganization() != null ? contact.getOrganization().getId() : null)
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .company(contact.getCompany())
                .department(contact.getDepartment())
                .designation(contact.getDesignation())
                .city(contact.getCity())
                .state(contact.getState())
                .country(contact.getCountry())
                .address(contact.getAddress())
                .postalCode(contact.getPostalCode())
                .website(contact.getWebsite())
                .tags(contact.getTags())
                .notes(contact.getNotes())
                .status(contact.getStatus())
                .createdBy(contact.getCreatedBy())
                .updatedBy(contact.getUpdatedBy())
                .createdAt(contact.getCreatedAt())
                .updatedAt(contact.getUpdatedAt())
                .build();

        dto.setImportBatchId(contact.getImportBatchId());
        dto.setImportBatchName(contact.getImportBatchName());
        dto.setImportDate(contact.getImportDate());
        dto.setSourceType(contact.getSourceType());
        dto.setEmailDomain(contact.getEmailDomain());

        return dto;
    }

    public void updateContactFromDto(Contact contact, UpdateContactRequestDto dto, Long updatedByUserId) {
        if (contact == null || dto == null) {
            return;
        }
        if (dto.getFirstName() != null) contact.setFirstName(trimOrNull(dto.getFirstName()));
        if (dto.getLastName() != null) contact.setLastName(trimOrNull(dto.getLastName()));
        if (dto.getPhone() != null) contact.setPhone(trimOrNull(dto.getPhone()));
        if (dto.getCompany() != null) contact.setCompany(trimOrNull(dto.getCompany()));
        if (dto.getDepartment() != null) contact.setDepartment(trimOrNull(dto.getDepartment()));
        if (dto.getDesignation() != null) contact.setDesignation(trimOrNull(dto.getDesignation()));
        if (dto.getCity() != null) contact.setCity(trimOrNull(dto.getCity()));
        if (dto.getState() != null) contact.setState(trimOrNull(dto.getState()));
        if (dto.getCountry() != null) contact.setCountry(trimOrNull(dto.getCountry()));
        if (dto.getAddress() != null) contact.setAddress(trimOrNull(dto.getAddress()));
        if (dto.getPostalCode() != null) contact.setPostalCode(trimOrNull(dto.getPostalCode()));
        if (dto.getWebsite() != null) contact.setWebsite(trimOrNull(dto.getWebsite()));
        if (dto.getTags() != null) contact.setTags(trimOrNull(dto.getTags()));
        if (dto.getNotes() != null) contact.setNotes(trimOrNull(dto.getNotes()));
        if (dto.getStatus() != null && !dto.getStatus().isBlank()) {
            contact.setStatus(dto.getStatus().trim().toUpperCase());
        }
        contact.setUpdatedBy(updatedByUserId);
    }

    private String trimOrNull(String str) {
        return (str != null && !str.isBlank()) ? str.trim() : null;
    }
}
