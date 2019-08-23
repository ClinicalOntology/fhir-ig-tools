package org.clinicalontology.fhir.tools.ig.api;

import java.io.File;
import java.util.List;

import org.clinicalontology.fhir.tools.ig.exception.JobRunnerException;

public interface FhirIgValidator {

	void validate() throws JobRunnerException;

	List<String> getValidatedResources() throws JobRunnerException;

	File getValidatedResource(String name) throws JobRunnerException;

	void init() throws JobRunnerException;

}
