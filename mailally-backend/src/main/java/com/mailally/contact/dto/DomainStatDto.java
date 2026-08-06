package com.mailally.contact.dto;

public class DomainStatDto {
    private String domain;
    private Long count;
    private Double percentage;

    public DomainStatDto() {
    }

    public DomainStatDto(String domain, Long count) {
        this.domain = domain;
        this.count = count;
    }

    public DomainStatDto(String domain, Long count, Double percentage) {
        this.domain = domain;
        this.count = count;
        this.percentage = percentage;
    }

    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    public Long getCount() { return count; }
    public void setCount(Long count) { this.count = count; }
    public Double getPercentage() { return percentage; }
    public void setPercentage(Double percentage) { this.percentage = percentage; }
}
