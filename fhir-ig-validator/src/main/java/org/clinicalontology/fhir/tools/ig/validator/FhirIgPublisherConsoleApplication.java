/**
 *
 */
package org.clinicalontology.fhir.tools.ig.validator;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

/**
 * @author dtsteven
 *
 */
public class FhirIgPublisherConsoleApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(FhirIgPublisherConsoleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		System.err.printf("Hello World\n");

	}

}
