/**
 *
 */
package org.clinicalontology.fhir.tools.ig.validator;

import java.io.File;

import org.clinicalontology.fhir.tools.ig.api.MessageManager;
import org.clinicalontology.fhir.tools.ig.exception.JobRunnerException;

import ca.uhn.fhir.parser.IParserErrorHandler;
import ca.uhn.fhir.parser.json.JsonLikeValue.ScalarType;
import ca.uhn.fhir.parser.json.JsonLikeValue.ValueType;

/**
 * @author dtsteven
 *
 */
public class ParseErrorHandler implements IParserErrorHandler {

	private final MessageManager messageManager;
	private File sourceFile;

	public ParseErrorHandler(MessageManager messageManager) {
		this.messageManager = messageManager;
	}

	public void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	}

	@Override
	public void containedResourceWithNoId(IParseLocation theLocation) {

		this.addError("Resource has contained child resource with no ID");
	}

	@Override
	public void incorrectJsonType(IParseLocation theLocation, String theElementName,
			ValueType theExpectedValueType, ScalarType theExpectedScalarType,
			ValueType theFoundValueType, ScalarType theFoundScalarType) {

		String message = this.createIncorrectJsonTypeMessage(theElementName,
				theExpectedValueType,
				theExpectedScalarType, theFoundValueType,
				theFoundScalarType);

		this.addError(message);
	}

	@Override
	public void invalidValue(IParseLocation theLocation, String theValue,
			String theError) {

		this.addError("Invalid attribute value: %s: %s", theValue,
				theError);
	}

	@Override
	public void missingRequiredElement(IParseLocation theLocation,
			String theElementName) {

		StringBuilder b = new StringBuilder();
		b.append("Resource is missing required element '");
		b.append(theElementName);
		b.append("'");
		if (theLocation != null) {
			b.append(" in parent element '");
			b.append(theLocation.getParentElementName());
			b.append("'");
		}
		this.addError(b.toString());
	}

	@Override
	public void unexpectedRepeatingElement(IParseLocation theLocation,
			String theElementName) {

		this.addError(
				"Multiple repetitions of non-repeatable element '%s' found during parse",
				theElementName);

	}

	@Override
	public void unknownAttribute(IParseLocation theLocation, String theAttributeName) {

		this.addError("Unknown attribute '%s' found during parse",
				theAttributeName);

	}

	@Override
	public void unknownElement(IParseLocation theLocation, String theElementName) {
		this.addError("Unknown element '%s' found during parse", theElementName);

	}

	@Override
	public void unknownReference(IParseLocation theLocation, String theReference) {
		this.addError("Resource has invalid reference: %s", theReference);

	}

	private void addError(String message, Object... args) {

		try {
			this.messageManager.addError(this.sourceFile, message, args);
		} catch (JobRunnerException e) {
		}
	}

	private String createIncorrectJsonTypeMessage(String theElementName,
			ValueType theExpected,
			ScalarType theExpectedScalarType, ValueType theFound,
			ScalarType theFoundScalarType) {
		StringBuilder b = new StringBuilder();
		b.append("Found incorrect type for element ");
		b.append(theElementName);
		b.append(" - Expected ");
		b.append(theExpected.name());
		if (theExpectedScalarType != null) {
			b.append(" (");
			b.append(theExpectedScalarType.name());
			b.append(")");
		}
		b.append(" and found ");
		b.append(theFound.name());
		if (theFoundScalarType != null) {
			b.append(" (");
			b.append(theFoundScalarType.name());
			b.append(")");
		}
		String message = b.toString();
		return message;
	}

}
