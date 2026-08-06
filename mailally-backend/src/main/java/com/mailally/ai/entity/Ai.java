package com.mailally.ai.entity;

import com.mailally.organization.entity.Organization;
import com.mailally.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Entity representing an AI prompt generation log entry in the ai_logs table.
 */
@Entity
@Table(name = "ai_logs")
public class Ai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Column(name = "prompt", nullable = false, columnDefinition = "TEXT")
    private String prompt;

    @Column(name = "prompt_type", nullable = false, length = 50)
    private String promptType; // SUBJECT, CONTENT, REWRITE, IMPROVE, GRAMMAR, SPAM_SCORE, CAMPAIGN_IDEA, CTA

    @Column(name = "response_content", columnDefinition = "LONGTEXT")
    private String responseContent;

    @Column(name = "provider", nullable = false, length = 50)
    private String provider; // MOCK, OPENAI, GEMINI, CLAUDE, GROQ, OLLAMA

    @Column(name = "tokens_used")
    private Integer tokensUsed;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    public Ai() {}

    public Ai(Long id, Organization organization, User user, String prompt, String promptType,
              String responseContent, String provider, Integer tokensUsed, LocalDateTime createdAt, Boolean isDeleted) {
        this.id = id;
        this.organization = organization;
        this.user = user;
        this.prompt = prompt;
        this.promptType = promptType;
        this.responseContent = responseContent;
        this.provider = provider;
        this.tokensUsed = tokensUsed;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public String getPromptType() { return promptType; }
    public void setPromptType(String promptType) { this.promptType = promptType; }
    public String getResponseContent() { return responseContent; }
    public void setResponseContent(String responseContent) { this.responseContent = responseContent; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public Integer getTokensUsed() { return tokensUsed; }
    public void setTokensUsed(Integer tokensUsed) { this.tokensUsed = tokensUsed; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
        if (this.provider == null) this.provider = "MOCK";
        if (this.promptType == null) this.promptType = "SUBJECT";
        if (this.tokensUsed == null) this.tokensUsed = 0;
        if (this.isDeleted == null) this.isDeleted = false;
    }

    public static AiBuilder builder() { return new AiBuilder(); }

    public static class AiBuilder {
        private Long id;
        private Organization organization;
        private User user;
        private String prompt;
        private String promptType;
        private String responseContent;
        private String provider;
        private Integer tokensUsed;
        private LocalDateTime createdAt;
        private Boolean isDeleted;

        AiBuilder() {}

        public AiBuilder id(Long id) { this.id = id; return this; }
        public AiBuilder organization(Organization organization) { this.organization = organization; return this; }
        public AiBuilder user(User user) { this.user = user; return this; }
        public AiBuilder prompt(String prompt) { this.prompt = prompt; return this; }
        public AiBuilder promptType(String promptType) { this.promptType = promptType; return this; }
        public AiBuilder responseContent(String responseContent) { this.responseContent = responseContent; return this; }
        public AiBuilder provider(String provider) { this.provider = provider; return this; }
        public AiBuilder tokensUsed(Integer tokensUsed) { this.tokensUsed = tokensUsed; return this; }
        public AiBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public AiBuilder isDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; return this; }

        public Ai build() {
            return new Ai(id, organization, user, prompt, promptType, responseContent, provider, tokensUsed, createdAt, isDeleted);
        }
    }
}
