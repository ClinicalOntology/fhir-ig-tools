/**
 *
 */
package org.clinicalontology.fhir.tools.ig.validator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.clinicalontology.fhir.tools.ig.api.FhirIgValidator;
import org.clinicalontology.fhir.tools.ig.api.MessageManager;
import org.clinicalontology.fhir.tools.ig.common.services.FhirIgResourceManager;
import org.clinicalontology.fhir.tools.ig.config.CommonConfiguration;
import org.clinicalontology.fhir.tools.ig.config.ValidatorConfiguration;
import org.clinicalontology.fhir.tools.ig.exception.JobRunnerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.validation.FhirValidator;

/**
 * @author dtsteven
 *
 */
@Component
public class FhirIgValidatorImpl implements FhirIgValidator {

	@Autowired
	private ValidatorConfiguration validatorConfiguration;

	@Autowired
	private MessageManager messageManager;

	@Autowired
	private FhirIgResourceManager resourceManager;

	@Autowired
	private CommonConfiguration commonConfiguration;

	private File validatedFolder;

	private FhirContext fhirContext;
	private FhirValidator validator;
	private IParser xmlParser;
	private ParseErrorHandler parseErrorHandler;

	@Override
	public void init() throws JobRunnerException {

		this.initFolders();

		this.initValidationEngine();

	}

	@Override
	public void validate() throws JobRunnerException {

		this.messageManager.setInterruptOnErrorFlag(this.validatorConfiguration
				.getInterruptOnError());

		// dummied up validation: simple copy validated file to destination folder
		for (String filename : this.resourceManager.getSelectedProjectMembers()) {
			File original = this.resourceManager.getSelectedProjectMember(filename);
			File copied = new File(this.validatedFolder, filename);
			try {
				FileUtils.copyFile(original, copied);
				this.messageManager.addInfo("Validated structure Definition: %s",
						filename);
			} catch (IOException e) {
				this.messageManager.addError(e, "Error copying file: %s", filename);

			}
		}

		this.messageManager.interruptOnError("Validation");

	}

	@Override
	public List<String> getValidatedResources() throws JobRunnerException {
		List<String> files = new ArrayList<>();

		String[] fileList;
		fileList = this.validatedFolder.list();

		for (String filename : fileList) {
			File file = new File(this.validatedFolder, filename);
			if (file.isFile()) {
				files.add(filename);
			}
		}

		return files;
	}

	@Override
	public File getValidatedResource(String filename) throws JobRunnerException {

		File file = new File(this.validatedFolder, filename);
		if (file.exists() && file.isFile()) {
			return file;
		} else {
			return null;
		}

	}

	private void initFolders() throws JobRunnerException {
		this.validatedFolder = new File(this.resourceManager.getArtifactsFolder(),
				this.validatorConfiguration.getValidatedPath());
		if (!this.validatedFolder.exists()) {
			this.validatedFolder.mkdir();
			this.messageManager.addInfo("Created %s",
					this.validatedFolder.getPath());
		}

		if (!this.validatedFolder.isDirectory()) {
			this.messageManager.addFatalError("%s is not a folder",
					this.validatedFolder.getPath());
		}

		// delete the contents of the validated folder
		try {
			FileUtils.cleanDirectory(this.validatedFolder);
		} catch (IOException e) {
			this.messageManager.addFatalError("Failure to clear %s", this.validatedFolder
					.getName());
		}
	}

	private void initValidationEngine() throws JobRunnerException {

		this.computeFhirContext();

		this.parseErrorHandler = new ParseErrorHandler(this.messageManager);

		this.xmlParser = this.fhirContext.newXmlParser();
		this.xmlParser.setParserErrorHandler(this.parseErrorHandler);

		// Ask the context for a validator
		this.validator = this.fhirContext.newValidator();
		this.validator.setValidateAgainstStandardSchema(true);
		this.validator.setValidateAgainstStandardSchematron(true);

	}

	private void computeFhirContext() throws JobRunnerException {

		if (this.commonConfiguration.getRelease() != null) {
			switch (this.commonConfiguration.getRelease().toLowerCase()) {
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
						this.commonConfiguration.getRelease());
			}
		}
	}
}
