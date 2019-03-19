package org.clinicalontology.fhir.tools.ig.api;

public interface FhirArtifactValidator<T> {

    void validate(T resource, MessageList messageList);

}
