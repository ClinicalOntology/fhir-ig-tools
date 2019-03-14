package org.clinicalontology.fhir.tools.ig.api;

import org.clinicalontology.fhir.tools.ig.constants.LogMessageLevel;

import java.util.List;

public interface MessageList {

    boolean isEmpty();

    boolean isNotEmpty();

    boolean hasErrors();

    List<Message> getErrors();

    void addErrors(List<Message> errors);

    void addError(Message error);

    boolean hasWarnings();

    List<Message> getWarnings();

    void addWarnings(List<Message> warning);

    void addWarning(Message warning);

    boolean hasInfo();

    List<Message> getInfo();

    void addInfo(List<Message> info);

    void addInfo(Message info);

    boolean hasErrorsOrWarnings();

    void addMessages(List<Message> messages);

    void addMessageOfType(List<Message> messages, LogMessageLevel  logMessageLevel);

    void index();
}
