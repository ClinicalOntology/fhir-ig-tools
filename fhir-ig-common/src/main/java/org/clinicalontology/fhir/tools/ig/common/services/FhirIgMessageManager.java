/**
 *
 */
package org.clinicalontology.fhir.tools.ig.common.services;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.clinicalontology.fhir.tools.ig.api.MessageApi;
import org.clinicalontology.fhir.tools.ig.api.MessageApi.Level;
import org.clinicalontology.fhir.tools.ig.api.MessageManagerApi;
import org.clinicalontology.fhir.tools.ig.model.FhirIgMessage;
import org.springframework.stereotype.Service;

/**
 * @author dtsteven
 *
 */
@Service
public class FhirIgMessageManager implements MessageManagerApi {

	private List<MessageApi> messages = new ArrayList<>();

	private EnumSet<Options> options;

	private int errorCount;

	private int warningCount;

	private int infoCount;

	private int debugCount;

	@Override
	public void reset() {
		this.messages.clear();
		this.errorCount = 0;
		this.warningCount = 0;
		this.infoCount = 0;
		this.debugCount = 0;
	}

	@Override
	public void setOptions(EnumSet<Options> options) {
		this.options = options;
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
		boolean add = false;

		switch (msg.getLevel()) {
		case WARNING:
			if (this.options.contains(Options.CAPTURE_WARNINGS)) {
				add = true;
				this.warningCount++;
			}
			break;

		case INFO:
			if (this.options.contains(Options.CAPTURE_INFO)) {
				add = true;
				this.infoCount++;
			}
			break;

		case DEBUG:
			if (this.options.contains(Options.CAPTURE_DEBUG)) {
				add = true;
				this.debugCount++;
			}
			break;

		case ERROR:
			this.errorCount++;
			add = true;
			break;

		default:
			break;

		}
		if (add) {
			this.messages.add(msg);
		}

	}

	@Override
	public void addError(String message, Object... args) {
		this.addMessage(new FhirIgMessage(Level.ERROR, message, args));

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
