package org.clinicalontology.fhir.tools.ig.api;

public interface FhirIgPublisher {

    void publish(MessageList messageList);

    boolean success();

    public boolean interruptOnError();

    public void setInterruptOnErrorFlag(boolean interruptOnError);

}
