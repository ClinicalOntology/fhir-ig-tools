package org.clinicalontology.fhir.tools.ig.api;

import org.clinicalontology.fhir.tools.ig.constants.LogMessageLevel;

public interface Message {

    LogMessageLevel getLevel();

    boolean isError();

    boolean isWarning();

    boolean isInfo();

    String getMessageText();

    boolean hasAssociatedException();

    RuntimeException getAssociatedException();
}
