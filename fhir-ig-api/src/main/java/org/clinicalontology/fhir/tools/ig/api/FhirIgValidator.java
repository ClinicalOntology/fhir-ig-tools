package org.clinicalontology.fhir.tools.ig.api;

import java.util.List;

public interface FhirIgValidator {

    void validate(MessageList messageList);

    boolean success();

    public boolean interruptOnError();

    public void setInterruptOnErrorFlag(boolean interruptOnError);

}
