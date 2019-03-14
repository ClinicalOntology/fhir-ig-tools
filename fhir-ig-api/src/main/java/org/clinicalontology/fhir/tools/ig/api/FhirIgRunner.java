package org.clinicalontology.fhir.tools.ig.api;

import java.util.List;

public interface FhirIgRunner {

    public List<Message> runJob();

    public boolean interruptOnError();

    public void setInterruptOnErrorFlag(boolean interruptOnError);

}
