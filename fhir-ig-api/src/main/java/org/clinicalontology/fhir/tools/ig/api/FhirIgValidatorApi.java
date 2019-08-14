package org.clinicalontology.fhir.tools.ig.api;

public interface FhirIgValidatorApi {

	void validate();

	boolean success();

	public boolean interruptOnError();

	public void setInterruptOnErrorFlag(boolean interruptOnError);

}
