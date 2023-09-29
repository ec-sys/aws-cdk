package com.myorg.todo.config;

import io.awspring.cloud.ses.SimpleEmailServiceMailSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
public class MailConfig {

    @Bean
    public MailSender mailSender(SesClient sesClient) {
        return new SimpleEmailServiceMailSender(sesClient);
    }
}
