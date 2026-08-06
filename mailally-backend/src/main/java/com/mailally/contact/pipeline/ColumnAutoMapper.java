package com.mailally.contact.pipeline;

import com.mailally.contact.provider.ContactRawRow;
import java.util.Map;

public class ColumnAutoMapper {

    public static ContactRawRow mapRow(Map<String, String> headerValueMap, int rowNumber) {
        ContactRawRow row = new ContactRawRow();
        row.setRowNumber(rowNumber);

        for (Map.Entry<String, String> entry : headerValueMap.entrySet()) {
            String header = normalizeHeader(entry.getKey());
            String val = entry.getValue() != null ? entry.getValue().trim() : "";

            if (isEmailHeader(header)) {
                row.setRawEmail(val);
            } else if (isFirstNameHeader(header)) {
                row.setFirstName(val);
            } else if (isLastNameHeader(header)) {
                row.setLastName(val);
            } else if (isPhoneHeader(header)) {
                row.setPhone(val);
            } else if (isCompanyHeader(header)) {
                row.setCompany(val);
            } else if (isDepartmentHeader(header)) {
                row.setDepartment(val);
            } else if (isDesignationHeader(header)) {
                row.setDesignation(val);
            } else if (isCityHeader(header)) {
                row.setCity(val);
            } else if (isStateHeader(header)) {
                row.setState(val);
            } else if (isCountryHeader(header)) {
                row.setCountry(val);
            } else if (isAddressHeader(header)) {
                row.setAddress(val);
            } else if (isPostalCodeHeader(header)) {
                row.setPostalCode(val);
            } else if (isWebsiteHeader(header)) {
                row.setWebsite(val);
            } else if (isTagsHeader(header)) {
                row.setTags(val);
            } else if (isNotesHeader(header)) {
                row.setNotes(val);
            } else if (isStatusHeader(header)) {
                row.setStatus(val);
            } else {
                row.getUnmappedFields().put(entry.getKey(), val);
            }
        }

        return row;
    }

    private static String normalizeHeader(String header) {
        if (header == null) return "";
        return header.trim().toLowerCase().replaceAll("[^a-z0-9]", "");
    }

    private static boolean isEmailHeader(String h) {
        return h.equals("email") || h.equals("emailaddress") || h.equals("mail") || h.equals("emailid") || h.equals("useremail");
    }

    private static boolean isFirstNameHeader(String h) {
        return h.equals("firstname") || h.equals("fname") || h.equals("givenname") || h.equals("first");
    }

    private static boolean isLastNameHeader(String h) {
        return h.equals("lastname") || h.equals("lname") || h.equals("surname") || h.equals("familyname") || h.equals("last");
    }

    private static boolean isPhoneHeader(String h) {
        return h.equals("phone") || h.equals("phonenumber") || h.equals("mobile") || h.equals("telephone") || h.equals("contactnumber") || h.equals("cell");
    }

    private static boolean isCompanyHeader(String h) {
        return h.equals("company") || h.equals("companyname") || h.equals("organization") || h.equals("org") || h.equals("business");
    }

    private static boolean isDepartmentHeader(String h) {
        return h.equals("department") || h.equals("dept") || h.equals("division");
    }

    private static boolean isDesignationHeader(String h) {
        return h.equals("designation") || h.equals("jobtitle") || h.equals("title") || h.equals("role") || h.equals("position");
    }

    private static boolean isCityHeader(String h) {
        return h.equals("city") || h.equals("town");
    }

    private static boolean isStateHeader(String h) {
        return h.equals("state") || h.equals("province") || h.equals("region");
    }

    private static boolean isCountryHeader(String h) {
        return h.equals("country") || h.equals("nation");
    }

    private static boolean isAddressHeader(String h) {
        return h.equals("address") || h.equals("streetaddress") || h.equals("street");
    }

    private static boolean isPostalCodeHeader(String h) {
        return h.equals("postalcode") || h.equals("zipcode") || h.equals("zip") || h.equals("postcode");
    }

    private static boolean isWebsiteHeader(String h) {
        return h.equals("website") || h.equals("url") || h.equals("domain") || h.equals("site");
    }

    private static boolean isTagsHeader(String h) {
        return h.equals("tags") || h.equals("tag") || h.equals("labels") || h.equals("category");
    }

    private static boolean isNotesHeader(String h) {
        return h.equals("notes") || h.equals("note") || h.equals("comments") || h.equals("description");
    }

    private static boolean isStatusHeader(String h) {
        return h.equals("status") || h.equals("subscriptionstatus") || h.equals("state");
    }
}
