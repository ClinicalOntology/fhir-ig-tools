package org.clinicalontology.fhir.tools.ig.api;

public interface FhirArtifactValidatorApi<T> {

	void validate(T resource);

}
