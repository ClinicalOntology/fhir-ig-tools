package org.clinicalontology.fhir.tools.ig.api;

import java.util.List;

public interface FhirIgRunner {

    public MessageList runJob();

    public boolean interruptOnError();

    public void setInterruptOnErrorFlag(boolean interruptOnError);

}
