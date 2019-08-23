package org.clinicalontology.fhir.tools.ig.common.model;

import org.clinicalontology.fhir.tools.ig.api.Message;

public class FhirIgMessage implements Message {

	private final String message;
	private final Level level;
	private Exception exception;

	public FhirIgMessage(Level level, String message, Object... args) {
		this.message = String.format(message, args);
		this.level = level;
		this.exception = null;

	}

	public FhirIgMessage(Exception exception, Level level, String message,
			Object... args) {
		this(level, message, args);
		this.exception = exception;

	}

	@Override
	public Level getLevel() {
		return this.level;
	}

	@Override
	public boolean isError() {
		return this.level.equals(Level.ERROR);
	}

	@Override
	public boolean isWarning() {
		return this.level.equals(Level.WARNING);
	}

	@Override
	public boolean isInfo() {
		return this.level.equals(Level.INFO);
	}

	@Override
	public boolean isDebug() {
		return this.level.equals(Level.DEBUG);
	}

	@Override
	public String getMessageText() {
		return this.message;
	}

	@Override
	public boolean hasAssociatedException() {
		return this.exception != null;
	}

	@Override
	public Exception getAssociatedException() {
		return this.exception;
	}
}
