/**
 *
 */
package org.clinicalontology.fhir.tools.ig.common.services;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.clinicalontology.fhir.tools.ig.api.MessageApi;
import org.clinicalontology.fhir.tools.ig.api.MessageApi.Level;
import org.clinicalontology.fhir.tools.ig.api.MessageManagerApi;
import org.clinicalontology.fhir.tools.ig.common.util.ElapsedTime;
import org.clinicalontology.fhir.tools.ig.model.FhirIgMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author dtsteven
 *
 */
@Service
public class FhirIgMessageManager implements MessageManagerApi {

	private Logger log = LoggerFactory.getLogger(FhirIgMessageManager.class);

	private List<MessageApi> messages;

	private int errorCount;

	private int warningCount;

	private int infoCount;

	private int debugCount;

	private ElapsedTime elapsedTime;

	@PostConstruct
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
	public boolean isEmpty() {
		return this.messages.size() == 0;
	}

	@Override
	public boolean isNotEmpty() {
		return this.messages.size() > 0;
	}

	@Override
	public List<MessageApi> getMessages() {
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
	public void addMessage(MessageApi msg) {

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
	public void addError(String message, Object... args) {
		this.addMessage(new FhirIgMessage(Level.ERROR, message, args));

	}

	@Override
	public void addError(Exception exception) {
		this.addMessage(new FhirIgMessage(exception, Level.ERROR, exception
				.getLocalizedMessage()));
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
