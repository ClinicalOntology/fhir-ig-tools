package org.clinicalontology.fhir.tools.ig.api;

import org.clinicalontology.fhir.tools.ig.exception.JobRunnerException;

public interface FhirIgPublisher {

	void publish() throws JobRunnerException;

	void init() throws JobRunnerException;

}
