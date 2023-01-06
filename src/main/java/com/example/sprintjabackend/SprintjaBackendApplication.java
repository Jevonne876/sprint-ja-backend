package com.example.sprintjabackend;

import com.example.sprintjabackend.model.User;
import com.example.sprintjabackend.repository.UserRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.persistence.Column;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.example.sprintjabackend.enums.Role.ROLE_USER;

@SpringBootApplication
public class SprintjaBackendApplication {

    private final UserRepository userRepository ;

    @Autowired
    public SprintjaBackendApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SprintjaBackendApplication.class, args);

    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("https://www.sprintja.com", "http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Origin", "Accept", "X-Requested-With",
                "Access-Control-Allow-Origin", "Content-Type", "Jwt-Token", "Authorization",
                "Access-Control-Request-Method", "Access-Control-Request-Headers", "Origin, Accept"));
        configuration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-Token", "Authorization",
                "Access-Control-Origin", "Access-Control-Origin", "Access-Control-Credentials"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

//    @Bean
//    public void fakeUsers(){
//        Faker faker = new Faker();
//        for (int i =0; i<=100; i ++){
//            User user = new User();
//            user.setTrn(faker.number().randomNumber());
//            user.setFirstName(faker.name().firstName());
//            user.setLastName(faker.name().lastName());
//            user.setDateOfBirth(faker.date().birthday());
//            user.setUsername(faker.name().username());
//            user.setEmail(faker.name().username());
//            user.setEmail(faker.phoneNumber().cellPhone());
//            user.setEmail(faker.address().streetAddress());
//            user.setEmail(faker.address().state());
//            userRepository.save(user);
//        }
//    }

}
