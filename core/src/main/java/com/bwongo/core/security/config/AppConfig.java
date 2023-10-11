package com.bwongo.core.security.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/6/23
 **/
@Configuration
@ComponentScan("com.bwongo.core.security.config")
@EnableJpaRepositories({"com.bwongo.core.user_mgt.repository",
                        "com.bwongo.core.school_mgt.repository",
                        "com.bwongo.core.student_mgt.repository",
                        "com.bwongo.core.vehicle_mgt.repository",
                        "com.bwongo.core.trip_mgt.repository",
                        "com.bwongo.core.base.repository"})
@EntityScan({"com.bwongo.core.user_mgt.model.jpa",
             "com.bwongo.core.school_mgt.model.jpa",
             "com.bwongo.core.base.model.jpa",
             "com.bwongo.core.vehicle_mgt.model.jpa",
             "com.bwongo.core.trip_mgt.model.jpa",
             "com.bwongo.core.student_mgt.model.jpa"})
public class AppConfig {
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

}
