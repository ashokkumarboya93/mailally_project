package com.mailally.ai.provider;

import com.mailally.ai.dto.AiGenerateRequestDto;
import com.mailally.ai.dto.AiResponseDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mock AI Provider Adapter delivering intelligent prompt template responses for subjects, body content, rewrites, grammar, spam scores, and CTAs.
 */
@Component
public class MockAiProviderAdapter implements AiProviderAdapter {

    public static final String PROVIDER_NAME = "MOCK";

    @Override
    public boolean supportsProvider(String providerName) {
        return providerName == null || providerName.isBlank() || PROVIDER_NAME.equalsIgnoreCase(providerName);
    }

    @Override
    public AiResponseDto generate(String promptType, AiGenerateRequestDto request) {
        String pType = promptType != null ? promptType.toUpperCase() : "SUBJECT";
        String prompt = request.getPrompt() != null ? request.getPrompt().trim() : "";
        String tone = request.getTone() != null ? request.getTone() : "PROFESSIONAL";

        String result;
        int tokens = 45 + prompt.length() / 4;

        switch (pType) {
            case "SUBJECT":
                result = "1. 🚀 Unlock Enterprise Potential: " + prompt + "\n" +
                         "2. Quick Question regarding " + prompt + "\n" +
                         "3. Exclusive Access: Transform your strategy today\n" +
                         "4. Don't Miss Out: " + prompt + " Update";
                break;
            case "CONTENT":
                result = "<div style=\"font-family: Arial, sans-serif;\">\n" +
                         "  <h2>Hello {{firstName}},</h2>\n" +
                         "  <p>We are thrilled to share an exciting update regarding <strong>" + prompt + "</strong>.</p>\n" +
                         "  <p>Our platform empowers enterprise teams to scale email marketing with unprecedented efficiency.</p>\n" +
                         "  <div style=\"margin: 20px 0;\">\n" +
                         "    <a href=\"{{actionUrl}}\" style=\"background-color: #4F46E5; color: white; padding: 12px 24px; text-decoration: none; border-radius: 6px;\">Explore Now</a>\n" +
                         "  </div>\n" +
                         "  <p>Best regards,<br>{{senderName}}</p>\n" +
                         "</div>";
                break;
            case "REWRITE":
                result = "Refined (" + tone + "): " + prompt + "\n\n" +
                         "Optimized for maximum click-through rate and subscriber engagement.";
                break;
            case "GRAMMAR":
                result = "Fixed & Polished: " + prompt;
                break;
            case "SPAM_SCORE":
                result = "Spam Score: 1.2/10 (EXCELLENT - Low Risk)\n\n" +
                         "✅ Zero high-risk trigger words detected ('FREE NOW', '100% Guaranteed').\n" +
                         "✅ Good text-to-HTML ratio.\n" +
                         "💡 Recommendation: Ensure unsubscribe links remain visible in footer.";
                break;
            case "CAMPAIGN_IDEA":
                result = "1. 'New Feature Spotlight' — Educate contacts on key features.\n" +
                         "2. 'Customer Success Showcase' — Highlight case studies & ROI.\n" +
                         "3. 'VIP Insider Digest' — Weekly newsletter curated for active subscribers.\n" +
                         "4. 'Limited Re-engagement Offer' — Target inactive audience segment.";
                break;
            case "CTA":
                result = "Suggested CTAs:\n" +
                         "• Claim Your Free Trial Today\n" +
                         "• Book Enterprise Demo Now\n" +
                         "• Upgrade Your Workspace\n" +
                         "• Start Automating Email Campaigns";
                break;
            default:
                result = "AI Output (" + pType + "): " + prompt;
                break;
        }

        return AiResponseDto.builder()
                .promptType(pType)
                .generatedContent(result)
                .provider(PROVIDER_NAME)
                .tokensUsed(tokens)
                .generatedAt(LocalDateTime.now())
                .build();
    }
}
