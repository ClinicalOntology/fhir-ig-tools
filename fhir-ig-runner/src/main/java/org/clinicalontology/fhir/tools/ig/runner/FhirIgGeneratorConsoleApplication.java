package org.clinicalontology.fhir.tools.ig.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FhirIgGeneratorConsoleApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(FhirIgGeneratorConsoleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		System.err.printf("Hello World\n");

	}

}
