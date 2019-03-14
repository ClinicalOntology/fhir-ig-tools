package org.clinicalontology.fhir.tools.ig.api;

import org.clinicalontology.fhir.tools.ig.model.ValidationLogMessage;

import java.util.List;

public interface FhirArtifactValidator<T> {

    List<ValidationLogMessage> validate(T resource);

}
