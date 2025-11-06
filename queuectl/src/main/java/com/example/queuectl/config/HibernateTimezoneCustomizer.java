//package com.example.queuectl.config;
//
//import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
//@Component
//public class HibernateTimezoneCustomizer implements HibernatePropertiesCustomizer {
//
//    @Override
//    public void customize(Map<String, Object> hibernateProperties) {
//        hibernateProperties.put("hibernate.jdbc.time_zone", "Asia/Kolkata");
//        System.out.println("✅ Hibernate JDBC timezone injected via HibernatePropertiesCustomizer: Asia/Kolkata");
//    }
//}
