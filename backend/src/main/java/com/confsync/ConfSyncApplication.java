package com.confsync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ConfSync — Smart Conference Management System
 * Project Based Learning in Java (PBLJ)
 *
 * Demonstrates:
 * - Core Java & Object-Oriented Programming (OOP)
 * - Java Collections (List, Set, Map, Stream API)
 * - Exception Handling (@RestControllerAdvice, Custom Exceptions)
 * - Local File Handling (Paper uploads & PDF Certificate generation)
 * - Database Connectivity (Spring Data JPA, Hibernate, MySQL)
 * - REST APIs, Spring Security, BCrypt, JWT Authentication
 */
@SpringBootApplication
public class ConfSyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfSyncApplication.class, args);
        System.out.println("=================================================");
        System.out.println("🚀 ConfSync Backend is successfully running!");
        System.out.println("🔗 Base URL: http://localhost:8080");
        System.out.println("🗄️ Database: MySQL localhost:3306/confsync_db");
        System.out.println("=================================================");
    }
}

