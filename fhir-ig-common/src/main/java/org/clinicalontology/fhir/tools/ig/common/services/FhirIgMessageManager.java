/**
 *
 */
package org.clinicalontology.fhir.tools.ig.common.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.clinicalontology.fhir.tools.ig.api.Message;
import org.clinicalontology.fhir.tools.ig.api.Message.Level;
import org.clinicalontology.fhir.tools.ig.api.MessageManager;
import org.clinicalontology.fhir.tools.ig.common.model.FhirIgMessage;
import org.clinicalontology.fhir.tools.ig.common.util.ElapsedTime;
import org.clinicalontology.fhir.tools.ig.exception.JobRunnerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author dtsteven
 *
 */
@Service
public class FhirIgMessageManager implements MessageManager {

	private Logger log = LoggerFactory.getLogger(FhirIgMessageManager.class);

	private List<Message> messages;

	private int errorCount;

	private int warningCount;

	private int infoCount;

	private int debugCount;

	private ElapsedTime elapsedTime;

	private boolean interruptOnError;
	// set when updating interruptOnError flag
	private int baseErrorCount;

	@Override
	public void init() {
		this.messages = new ArrayList<>();
		this.elapsedTime = new ElapsedTime();
	}

	@Override
	public void reset() {
		this.messages.clear();
		this.errorCount = 0;
		this.warningCount = 0;
		this.infoCount = 0;
		this.debugCount = 0;
		this.elapsedTime.reset();
	}

	@Override
	public void setInterruptOnErrorFlag(boolean state) {
		this.interruptOnError = state;
		this.baseErrorCount = this.errorCount;
	}

	@Override
	public void interruptOnError(String module) throws JobRunnerException {
		if (this.errorCount > this.baseErrorCount) {
			throw new JobRunnerException("Errors Present in " + module);
		}

	}

	@Override
	public boolean isEmpty() {
		return this.messages.size() == 0;
	}

	@Override
	public boolean isNotEmpty() {
		return this.messages.size() > 0;
	}

	@Override
	public List<Message> getMessages() {
		return this.messages;
	}

	@Override
	public int getErrorCount() {
		return this.errorCount;
	}

	@Override
	public int getWarningCount() {
		return this.warningCount;
	}

	@Override
	public int getInfoCount() {
		return this.infoCount;
	}

	@Override
	public int getDebugCount() {
		return this.debugCount;
	}

	@Override
	public void addMessage(Message msg) {

		String text = String.format("(%s) %s",
				this.elapsedTime.toString(),
				msg.getMessageText());

		switch (msg.getLevel()) {
		case WARNING:
			if (this.log.isWarnEnabled()) {
				this.log.warn(text);
				this.messages.add(msg);
				this.warningCount++;
			}
			break;

		case INFO:
			if (this.log.isInfoEnabled()) {
				this.log.info(text);
				this.messages.add(msg);
				this.infoCount++;
			}
			break;

		case DEBUG:
			if (this.log.isDebugEnabled()) {
				this.log.info(text);
				this.messages.add(msg);
				this.debugCount++;
			}
			break;

		case ERROR:
			this.errorCount++;
			this.log.error(text);
			this.messages.add(msg);
			break;

		default:
			break;

		}
	}

	@Override
	public void addError(String message, Object... args) throws JobRunnerException {
		FhirIgMessage fhirIgMessage = new FhirIgMessage(Level.ERROR, message, args);
		this.addMessage(fhirIgMessage);
		if (this.interruptOnError) {
			throw new JobRunnerException(fhirIgMessage.getMessageText());
		}

	}

	@Override
	public void addError(File source, String message, Object... args)
			throws JobRunnerException {
		String localMessage = String.format(message, args);
		FhirIgMessage fhirIgMessage = new FhirIgMessage(Level.ERROR, "%s: %s", source
				.getName(), localMessage);
		this.addMessage(fhirIgMessage);
		if (this.interruptOnError) {
			throw new JobRunnerException(fhirIgMessage.getMessageText());
		}

	}

	@Override
	public void addFatalError(String message, Object... args) throws JobRunnerException {
		FhirIgMessage fhirIgMessage = new FhirIgMessage(Level.ERROR, message, args);
		this.addMessage(fhirIgMessage);
		throw new JobRunnerException(fhirIgMessage.getMessageText());
	}

	@Override
	public void addError(Exception exception) throws JobRunnerException {
		FhirIgMessage fhirIgMessage = new FhirIgMessage(exception, Level.ERROR, exception
				.getLocalizedMessage());
		this.addMessage(fhirIgMessage);
		if (this.interruptOnError) {
			throw new JobRunnerException(fhirIgMessage.getMessageText(), exception);
		}
	}

	@Override
	public void addError(Exception exception, String message, Object... args)
			throws JobRunnerException {
		FhirIgMessage fhirIgMessage = new FhirIgMessage(exception, Level.ERROR, message,
				args);
		this.addMessage(fhirIgMessage);
		if (this.interruptOnError) {
			throw new JobRunnerException(fhirIgMessage.getMessageText(), exception);
		}
	}

	@Override
	public void addWarning(String message, Object... args) {
		this.addMessage(new FhirIgMessage(Level.WARNING, message, args));
	}

	@Override
	public void addInfo(String message, Object... args) {
		this.addMessage(new FhirIgMessage(Level.INFO, message, args));
	}

	@Override
	public void addDebug(String message, Object... args) {
		this.addMessage(new FhirIgMessage(Level.DEBUG, message, args));
	}

}
