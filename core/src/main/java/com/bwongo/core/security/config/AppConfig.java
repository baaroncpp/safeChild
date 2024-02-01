package com.bwongo.core.security.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.Executor;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/6/23
 **/
@EnableAsync
@Configuration
@ComponentScan("com.bwongo.core.security.config")
@EnableJpaRepositories({"com.bwongo.core.user_mgt.repository",
                        "com.bwongo.core.school_mgt.repository",
                        "com.bwongo.core.student_mgt.repository",
                        "com.bwongo.core.vehicle_mgt.repository",
                        "com.bwongo.core.trip_mgt.repository",
                        "com.bwongo.core.notify_mgt.repository",
                        "com.bwongo.core.account_mgt.repository",
                        "com.bwongo.core.base.repository"})
@EntityScan({"com.bwongo.core.user_mgt.model.jpa",
             "com.bwongo.core.school_mgt.model.jpa",
             "com.bwongo.core.base.model.jpa",
             "com.bwongo.core.vehicle_mgt.model.jpa",
             "com.bwongo.core.trip_mgt.model.jpa",
             "com.bwongo.core.account_mgt.model.jpa",
             "com.bwongo.core.notify_mgt.model.jpa",
             "com.bwongo.core.student_mgt.model.jpa"})
public class AppConfig {
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public WebClient.Builder getWebClientBuilder(){
        return WebClient.builder();
    }

    @Bean
    public WebClient getWebClient(){
        return WebClient.create();
    }

    @Bean(name = "asyncTaskExecutor")
    public Executor asyncTaskExecutor(){
        var taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(4);
        taskExecutor.setQueueCapacity(150);
        taskExecutor.setMaxPoolSize(4);
        taskExecutor.setThreadNamePrefix("AsyncTaskThread");
        taskExecutor.initialize();

        return taskExecutor;
    }

   /* @Bean
    public JavaMailSenderImpl mailSender() {
        return new JavaMailSenderImpl();
*//*
        javaMailSender.setProtocol("SMTP");
        javaMailSender.setHost("127.0.0.1");
        javaMailSender.setPort(25);

        return javaMailSender;*//*
    }*/
}
