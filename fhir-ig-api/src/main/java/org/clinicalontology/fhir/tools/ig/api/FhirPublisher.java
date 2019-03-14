package org.clinicalontology.fhir.tools.ig.api;

import java.util.List;

public interface FhirPublisher {

    List<Message> publish();

    boolean success();

}
