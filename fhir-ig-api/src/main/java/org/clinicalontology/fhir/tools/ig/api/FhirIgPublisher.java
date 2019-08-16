package org.clinicalontology.fhir.tools.ig.api;

public interface FhirIgPublisher {

	void publish();

	boolean success();

	public boolean interruptOnError();

	public void setInterruptOnErrorFlag(boolean interruptOnError);

}
