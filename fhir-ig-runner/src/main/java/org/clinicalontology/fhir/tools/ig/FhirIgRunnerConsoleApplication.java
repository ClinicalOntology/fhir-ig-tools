package org.clinicalontology.fhir.tools.ig;

import org.clinicalontology.fhir.tools.ig.api.FhirIgRunnerApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FhirIgRunnerConsoleApplication implements CommandLineRunner {

	@Autowired
	private FhirIgRunnerApi runner;

	public static void main(String[] args) {
		SpringApplication.run(FhirIgRunnerConsoleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		this.runner.runJob();
	}

}
