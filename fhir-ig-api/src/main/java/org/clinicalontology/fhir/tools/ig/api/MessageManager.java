package org.clinicalontology.fhir.tools.ig.api;

import java.util.List;

import org.clinicalontology.fhir.tools.ig.exception.JobRunnerException;

public interface MessageManager {

	void reset();

	/**
	 * enable/disable throw exception on error
	 *
	 * @param state -- true enable, false disable
	 */
	void setInterruptOnErrorFlag(boolean state);

	/**
	 * throw an exception if errors are present
	 */
	void interruptOnError(String module) throws JobRunnerException;

	boolean isEmpty();

	boolean isNotEmpty();

	int getErrorCount();

	int getWarningCount();

	int getInfoCount();

	int getDebugCount();

	List<Message> getMessages();

	void addMessage(Message msg);

	void addError(String message, Object... args) throws JobRunnerException;

	void addError(Exception exception) throws JobRunnerException;

	void addWarning(String message, Object... args);

	void addInfo(String message, Object... args);

	void addDebug(String message, Object... args);

}
