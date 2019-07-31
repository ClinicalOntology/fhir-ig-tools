/**
 *
 */
package org.clinicalontology.fhir.tools.ig.publisher;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author dtsteven
 *
 */
@SpringBootApplication
public class FhirIgPublisherConsoleApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(FhirIgPublisherConsoleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		System.err.printf("Hello World\n");

	}

}
