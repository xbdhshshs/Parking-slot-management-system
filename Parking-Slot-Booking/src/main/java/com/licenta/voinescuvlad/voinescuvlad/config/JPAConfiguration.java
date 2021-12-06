package com.licenta.voinescuvlad.voinescuvlad.config;

import com.licenta.voinescuvlad.voinescuvlad.services.ParkingService;
import com.licenta.voinescuvlad.voinescuvlad.services.ParkingServiceImpl;
import com.licenta.voinescuvlad.voinescuvlad.services.UserService;
import com.licenta.voinescuvlad.voinescuvlad.services.UserServiceImpl;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.licenta.voinescuvlad.voinescuvlad.repositories"})
@ComponentScan("com.licenta.voinescuvlad.voinescuvlad")
@PropertySource("classpath:application.properties")
public class JPAConfiguration {
    @Bean
    public UserService userService(){
      return new UserServiceImpl();
    }



}