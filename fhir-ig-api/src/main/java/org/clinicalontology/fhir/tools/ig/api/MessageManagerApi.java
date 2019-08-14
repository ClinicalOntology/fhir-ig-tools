package org.clinicalontology.fhir.tools.ig.api;

import java.util.EnumSet;
import java.util.List;

public interface MessageManagerApi {

	public enum Options {
		CAPTURE_WARNINGS, // capture warning messages
		CAPTURE_INFO, // capture info messages
		CAPTURE_DEBUG, // capture debug messages
	};

	public void setOptions(EnumSet<Options> options);

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

	void addWarning(String message, Object... args);

	void addInfo(String message, Object... args);

	void addDebug(String message, Object... args);

}
