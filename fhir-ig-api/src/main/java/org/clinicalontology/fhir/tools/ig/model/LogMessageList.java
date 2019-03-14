package org.clinicalontology.fhir.tools.ig.model;

import org.clinicalontology.fhir.tools.ig.api.Message;
import org.clinicalontology.fhir.tools.ig.api.MessageList;
import org.clinicalontology.fhir.tools.ig.constants.LogMessageLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LogMessageList implements MessageList {

    private List<Message> messages = new ArrayList<Message>();
    private Map<LogMessageLevel, List<Message>> index = new HashMap<>();

    public boolean isEmpty() {
        return messages == null || messages.size() == 0;
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public boolean hasErrors() {
        return messages != null && !messages.stream().filter(message -> message.getLevel() == LogMessageLevel.ERROR).collect(Collectors.toList()).isEmpty();
    }

    public List<Message> getErrors() {
        return messages.stream().filter(message -> message.getLevel() == LogMessageLevel.ERROR).collect(Collectors.toList());
    }

    public void addErrors(List<Message> errors) {
        messages.addAll(errors.stream().filter(message -> message.getLevel() == LogMessageLevel.ERROR).collect(Collectors.toList()));
    }

    public void addError(Message error) {
        if(error.getLevel() == LogMessageLevel.ERROR) {
            messages.add(error);
        }
    }

    public boolean hasWarnings() {
        return messages != null && !messages.stream().filter(message -> message.getLevel() == LogMessageLevel.WARNING).collect(Collectors.toList()).isEmpty();
    }

    public List<Message> getWarnings() {
        return messages.stream().filter(message -> message.getLevel() == LogMessageLevel.WARNING).collect(Collectors.toList());
    }

    public void addWarnings(List<Message> warning) {
        messages.addAll(warning.stream().filter(message -> message.getLevel() == LogMessageLevel.WARNING).collect(Collectors.toList()));
    }

    public void addWarning(Message warning) {
        if(warning.getLevel() == LogMessageLevel.WARNING) {
            messages.add(warning);
        }
    }

    public boolean hasInfo() {
        return messages != null && !messages.stream().filter(message -> message.getLevel() == LogMessageLevel.INFO).collect(Collectors.toList()).isEmpty();
    }

    public List<Message> getInfo() {
        return messages.stream().filter(message -> message.getLevel() == LogMessageLevel.INFO).collect(Collectors.toList());
    }

    public void addInfo(List<Message> info) {
        messages.addAll(info.stream().filter(message -> message.getLevel() == LogMessageLevel.INFO).collect(Collectors.toList()));
    }

    public void addInfo(Message info) {
        if(info.getLevel() == LogMessageLevel.WARNING) {
            messages.add(info);
        }
    }

    public boolean hasErrorsOrWarnings() {
        return hasErrors() || hasWarnings();
    }

    public void addMessages(List<Message> messages) {
        this.messages.addAll(messages);
    }

    public void addMessageOfType(List<Message> messages, LogMessageLevel logMessageLevel) {
        this.messages.addAll(messages.stream().filter(message -> message.getLevel() == logMessageLevel).collect(Collectors.toList()));
    }

    @Override
    public void index() {

        index.clear();

        for(Message message : messages) {
            List<Message> messageList = index.get(message.getLevel());
            if(messageList == null) {
                messageList = new ArrayList<>();
                index.put(message.getLevel(), messageList);
            }
            messageList.add(message);
        }

    }
}
