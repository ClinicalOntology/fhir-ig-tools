/**
 *
 */
package org.clinicalontology.fhir.tools.ig.common.services;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.clinicalontology.fhir.tools.ig.api.CommonServices;
import org.clinicalontology.fhir.tools.ig.api.MessageManager;
import org.clinicalontology.fhir.tools.ig.exception.JobRunnerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.validation.FhirValidator;

/**
 * @author dtsteven
 *
 */
@Service
public class FhirIgCommonServices implements CommonServices {

	@Autowired
	private MessageManager messageManager;
	@Autowired
	private ParseErrorHandler parseErrorHandler;
	@Autowired
	private FhirIgResourceManager resourceManager;

	private FhirContext fhirContext;
	private IParser xmlParser;
	private FhirValidator validator;

	@Override
	public void init() throws JobRunnerException {
		this.computeFhirContext();
	}

	public FhirContext getFhirContext() {
		return this.fhirContext;
	}

	public IParser getXmlParser() {
		return this.xmlParser;
	}

	public FhirValidator getValidator() {
		return this.validator;
	}

	public void setNarrativeGenerator(INarrativeGenerator gen) {
		this.fhirContext.setNarrativeGenerator(gen);
	}

	public void resetNarrativeGenerator() {
		this.fhirContext.setNarrativeGenerator(null);
	}

	private void computeFhirContext() throws JobRunnerException {

		if (this.resourceManager.getVersion() != null) {
			switch (this.resourceManager.getVersion().toLowerCase()) {
			case "dstu3":
				this.fhirContext = FhirContext.forDstu3();
				break;
			case "r4":
				this.fhirContext = FhirContext.forR4();
				break;
			default:
				break;
			}

			if (this.fhirContext == null) {
				this.messageManager.addError(
						"Unknown value for ig.release: %s.  Must be one of 'dstu3,r4'",
						this.resourceManager.getVersion());
			} else {
				this.xmlParser = this.fhirContext.newXmlParser();
				this.parseErrorHandler = new ParseErrorHandler(this.messageManager);

				this.xmlParser.setParserErrorHandler(this.parseErrorHandler);
				this.validator = this.fhirContext.newValidator();

				this.validator.setValidateAgainstStandardSchema(
						true);
				this.validator.setValidateAgainstStandardSchematron(false);

			}
		}
	}

	/**
	 * find or create a path (each represented as an argument) with a parent
	 * specified.
	 *
	 * @param parent
	 * @param folders list of child folders
	 * @return Created folder
	 * @throws JobRunnerException
	 */
	public File findOrCreateFolder(File parent, String... folders)
			throws JobRunnerException {
		File folder = parent;
		for (String folderName : folders) {
			folder = new File(folder, folderName);
			if (!folder.exists()) {
				folder.mkdir();
				this.messageManager.addInfo("Created %s",
						folder.getPath());
			}

			if (!folder.isDirectory()) {
				this.messageManager.addFatalError("%s is not a folder",
						folder.getPath());
			}
		}
		return folder;
	}

	public void createFolderIfNecessary(File folder) throws JobRunnerException {
		if (!folder.exists()) {
			folder.mkdir();
			this.messageManager.addInfo("Created %s",
					folder.getPath());
		}

		if (!folder.isDirectory()) {
			this.messageManager.addFatalError("%s is not a folder",
					folder.getPath());
		}

	}

	public void resetFolder(File folder) throws JobRunnerException {
		// delete the contents of the project validated folder
		try {
			FileUtils.cleanDirectory(folder);
		} catch (IOException e) {
			this.messageManager.addFatalError("Failure to clear %s", folder.getName());
		}

	}

}
