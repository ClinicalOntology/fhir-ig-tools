package org.clinicalontology.fhir.tools.ig.api;

import org.clinicalontology.fhir.tools.ig.exception.JobRunnerException;

public interface FhirIgValidator {

	void validate() throws JobRunnerException;

	boolean success();

}
