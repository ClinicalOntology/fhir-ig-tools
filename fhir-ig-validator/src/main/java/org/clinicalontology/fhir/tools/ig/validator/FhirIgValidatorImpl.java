/**
 *
 */
package org.clinicalontology.fhir.tools.ig.validator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;

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

	// dest folder for validated files
	private File projectValidatedFolder;

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
				.getInterruptIfErrorOnResource());

		for (String filename : this.resourceManager.getSelectedProjectMembers()) {

			int errorCount = this.messageManager.getErrorCount();
			File original = this.resourceManager.getSelectedProjectMember(filename);
			this.parseErrorHandler.setSourceFile(original);
			this.validateResource(original);

			if (errorCount == this.messageManager.getErrorCount()) {
				this.messageManager.addInfo("Validation succeeded: %s", filename);
				this.putResourceInValidatedFolder(original, filename);
			} else {
				this.messageManager.addInfo("Validation failed: %s", filename);
			}
		}

		if (this.validatorConfiguration.getInterruptIfErrorOnModule()) {
			this.messageManager.interruptOnError("Validation");
		}

	}

	private void validateResource(File original) throws JobRunnerException {

		try {
			IBaseResource sd = this.xmlParser.parseResource(new FileReader(original));

			ValidationResult result = this.validator.validateWithResult(sd);

			for (SingleValidationMessage message : result.getMessages()) {
				this.messageManager.addError(original, message.getMessage());
			}

		} catch (ConfigurationException | DataFormatException | FileNotFoundException e) {
			this.messageManager.addError(e, e.getLocalizedMessage());
		}

	}

	@Override
	public List<String> getValidatedResources() throws JobRunnerException {
		List<String> files = new ArrayList<>();

		String[] fileList;
		fileList = this.projectValidatedFolder.list();

		for (String filename : fileList) {
			File file = new File(this.projectValidatedFolder, filename);
			if (file.isFile()) {
				files.add(filename);
			}
		}

		return files;
	}

	@Override
	public File getValidatedResource(String filename) throws JobRunnerException {

		File file = new File(this.projectValidatedFolder, filename);
		if (file.exists() && file.isFile()) {
			return file;
		} else {
			return null;
		}

	}

	private void initFolders() throws JobRunnerException {
		File validatedFolder = new File(this.resourceManager.getArtifactsFolder(),
				this.validatorConfiguration.getValidatedPath());

		this.createFolderIfNecessary(validatedFolder);

		// now find the project specific validated folder
		this.projectValidatedFolder = new File(validatedFolder, this.resourceManager
				.getSelectedProjectFolder());

		this.createFolderIfNecessary(this.projectValidatedFolder);

		// delete the contents of the project validated folder
		try {
			FileUtils.cleanDirectory(this.projectValidatedFolder);
		} catch (IOException e) {
			this.messageManager.addFatalError("Failure to clear %s",
					this.projectValidatedFolder
							.getName());
		}
	}

	private void createFolderIfNecessary(File folder) throws JobRunnerException {
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

	private void putResourceInValidatedFolder(File original, String filename)
			throws JobRunnerException {

		File copied = new File(this.projectValidatedFolder, filename);
		try {
			FileUtils.copyFile(original, copied);
		} catch (IOException e) {
			this.messageManager.addError(e, "Error copying file: %s", filename);

		}
	}

	private void initValidationEngine() throws JobRunnerException {

		this.computeFhirContext();

		this.parseErrorHandler = new ParseErrorHandler(this.messageManager);

		this.xmlParser = this.fhirContext.newXmlParser();
		this.xmlParser.setParserErrorHandler(this.parseErrorHandler);

		// Ask the context for a validator
		this.validator = this.fhirContext.newValidator();
		// set config options for validator
		this.validator.setValidateAgainstStandardSchema(
				this.validatorConfiguration.getValidateOnSchema());
		this.validator.setValidateAgainstStandardSchematron(
				this.validatorConfiguration.getValidateOnSchematron());

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
