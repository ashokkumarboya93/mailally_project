package com.mailally;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.*;

public class SesDirectTest {

    @Test
    public void testSesDirectSend() {
        String accessKey = System.getenv("AWS_SES_ACCESS_KEY") != null ? System.getenv("AWS_SES_ACCESS_KEY") : "YOUR_AWS_SES_ACCESS_KEY";
        String secretKey = System.getenv("AWS_SES_SECRET_KEY") != null ? System.getenv("AWS_SES_SECRET_KEY") : "YOUR_AWS_SES_SECRET_KEY";
        String region = System.getenv("AWS_SES_REGION") != null ? System.getenv("AWS_SES_REGION") : "ap-south-1";

        System.out.println("Testing Direct AWS SES v2 API dispatch...");

        SesV2Client client = SesV2Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();

        try {
            SendEmailRequest request = SendEmailRequest.builder()
                    .fromEmailAddress("Marcamor Team <info@marcamor.com>")
                    .destination(Destination.builder().toAddresses("ashokkumarboya93@gmail.com").build())
                    .content(EmailContent.builder()
                            .simple(Message.builder()
                                    .subject(Content.builder().data("AWS SES API Direct Delivery Test").charset("UTF-8").build())
                                    .body(Body.builder().html(Content.builder().data("<h2>AWS SES API v2 Test Success!</h2><p>Sent directly via AWS SES SDK API v2 in under 200ms.</p>").charset("UTF-8").build()).build())
                                    .build())
                            .build())
                    .build();

            long start = System.currentTimeMillis();
            SendEmailResponse response = client.sendEmail(request);
            long end = System.currentTimeMillis();

            System.out.println("SUCCESS! Email sent via AWS SES API v2 in " + (end - start) + " ms!");
            System.out.println("MessageId: " + response.messageId());
        } catch (Exception ex) {
            System.err.println("FAILED AWS SES API TEST: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
