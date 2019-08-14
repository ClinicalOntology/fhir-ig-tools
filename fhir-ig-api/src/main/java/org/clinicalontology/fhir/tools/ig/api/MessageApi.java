package org.clinicalontology.fhir.tools.ig.api;

public interface MessageApi {

	public enum Level {
		DEBUG,
		INFO,
		WARNING,
		ERROR;
	}

	Level getLevel();

	boolean isError();

	boolean isWarning();

	boolean isInfo();

	boolean isDebug();

	String getMessageText();

	boolean hasAssociatedException();

	Exception getAssociatedException();
}
