package org.clinicalontology.fhir.tools.ig.api;

import java.util.List;

public interface MessageManagerApi {

	void reset();

	boolean isEmpty();

	boolean isNotEmpty();

	int getErrorCount();

	int getWarningCount();

	int getInfoCount();

	int getDebugCount();

	List<MessageApi> getMessages();

	void addMessage(MessageApi msg);

	void addError(String message, Object... args);

	void addError(Exception exception);

	void addWarning(String message, Object... args);

	void addInfo(String message, Object... args);

	void addDebug(String message, Object... args);

}
