package com.inditex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Inditex application.
 * <p>
 * This class contains the main method which bootstraps the Spring Boot application.
 * </p>
 */

@SpringBootApplication
public class InditexApp {

	/**
	 * Main method to run the Spring Boot application.
	 *
	 * @param args the command-line arguments
	 */

	public static void main(String[] args) {
		SpringApplication.run(InditexApp.class, args);
	}
}
