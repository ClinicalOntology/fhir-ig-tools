package org.clinicalontology.fhir.tools.ig.runner;

import org.clinicalontology.fhir.tools.ig.api.FhirIgPublisher;
import org.clinicalontology.fhir.tools.ig.api.FhirIgRunner;
import org.clinicalontology.fhir.tools.ig.api.FhirIgValidator;
import org.clinicalontology.fhir.tools.ig.api.MessageList;
import org.clinicalontology.fhir.tools.ig.model.LogMessageList;

public class FhirIgGenerator implements FhirIgRunner {

    private boolean interruptOnError = true;

    private FhirIgValidator validator;

    private FhirIgPublisher publisher;

    public MessageList runJob() {

        MessageList logs = new LogMessageList();

        validator.setInterruptOnErrorFlag(interruptOnError);
        publisher.setInterruptOnErrorFlag(interruptOnError);

        validator.validate(logs);
        publisher.publish(logs);

        return logs;
    }

    public boolean interruptOnError() {
        return interruptOnError;
    }

    public void setInterruptOnErrorFlag(boolean interruptOnError) {
        this.interruptOnError = interruptOnError;
    }
}
