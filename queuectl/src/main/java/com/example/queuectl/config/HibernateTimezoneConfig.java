//package com.example.queuectl.config;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.TimeZone;
//import java.util.Properties;
//
//@Configuration
//public class HibernateTimezoneConfig {
//
//    @PostConstruct
//    public void init() {
//        // Force the JVM timezone globally
//        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
//
//        // Force Hibernate to use IST at JDBC level
//        System.setProperty("hibernate.jdbc.time_zone", "Asia/Kolkata");
//
//        System.out.println("✅ Hibernate JDBC timezone set programmatically: Asia/Kolkata");
//    }
//}
