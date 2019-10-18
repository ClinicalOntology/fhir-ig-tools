package org.clinicalontology.fhir.tools.ig.api;

import java.io.File;
import java.util.List;

import org.clinicalontology.fhir.tools.ig.exception.JobRunnerException;

public interface MessageManager {

	/**
	 * initialize the object
	 */
	void init();

	/**
	 * reset the object to original state
	 */
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

	public void addError(Exception exception, String message, Object... args)
			throws JobRunnerException;

	/**
	 * A fatal error means that progress cannot continue. It will always throw an
	 * exception
	 *
	 * @param message
	 * @param args
	 * @throws JobRunnerException
	 */
	void addFatalError(String message, Object... args) throws JobRunnerException;

	void addWarning(String message, Object... args);

	void addInfo(String message, Object... args);

	void addDebug(String message, Object... args);

	void addError(File source, String message, Object... args) throws JobRunnerException;

}
