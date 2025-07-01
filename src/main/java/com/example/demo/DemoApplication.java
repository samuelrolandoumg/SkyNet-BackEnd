//package com.example.demo;
//
//import io.github.cdimascio.dotenv.Dotenv;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//
//@SpringBootApplication
//@ComponentScan(basePackages = {"controller", "services", "service.impl", "repository", "models"})
//@EntityScan(basePackages = {"models"})
//@EnableJpaRepositories(basePackages = {"repository"})
//public class DemoApplication {
//    public static void main(String[] args) {
//        
//                // ✅ Carga manual de .env
//        Dotenv dotenv = Dotenv.configure()
//                              .filename(".env") // Asegúrate que el archivo se llame así
//                              .load();
//
//        // ✅ Inyectamos al entorno de Java para que Spring los reconozca
//        System.setProperty("DB_URL", dotenv.get("DB_URL"));
//        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
//        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
//        SpringApplication.run(DemoApplication.class, args);
//    }
//}

package com.example.demo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"controller", "services", "service.impl", "repository", "models", "util", "exceptions", "config"})
@EntityScan(basePackages = {"models"})
@EnableJpaRepositories(basePackages = {"repository"})
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
