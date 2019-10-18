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
import org.clinicalontology.fhir.tools.ig.common.services.FhirIgCommonServices;
import org.clinicalontology.fhir.tools.ig.common.services.FhirIgResourceManager;
import org.clinicalontology.fhir.tools.ig.common.services.ParseErrorHandler;
import org.clinicalontology.fhir.tools.ig.config.ValidatorConfiguration;
import org.clinicalontology.fhir.tools.ig.exception.JobRunnerException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.parser.DataFormatException;
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
	private OperationOutcomePublisher outcomePublisher;

	@Autowired
	private FhirIgCommonServices commonServices;

	@Autowired
	private ParseErrorHandler parseErrorHandler;

	// dest folder for validated files
	private File projectResourcesFolder;
	private File projectOutcomesFolder;

	@Override
	public void init() throws JobRunnerException {

		this.initValidationEngine();
		this.initFolders();
	}

	@Override
	public void validate() throws JobRunnerException {

		this.commonServices.resetFolder(this.projectResourcesFolder);

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
			IBaseResource sd = this.commonServices.getXmlParser().parseResource(new FileReader(
					original));

			ValidationResult result = this.commonServices.getValidator().validateWithResult(sd);

			this.outcomePublisher.publish(this.projectOutcomesFolder, original, result);

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
		fileList = this.projectResourcesFolder.list();

		for (String filename : fileList) {
			File file = new File(this.projectResourcesFolder, filename);
			if (file.isFile()) {
				files.add(filename);
			}
		}

		return files;
	}

	@Override
	public File getValidatedResource(String filename) throws JobRunnerException {

		File file = new File(this.projectResourcesFolder, filename);
		if (file.exists() && file.isFile()) {
			return file;
		} else {
			return null;
		}

	}

	private void initFolders() throws JobRunnerException {

		// find the publish folder
		File validationFolder = this.commonServices.findOrCreateFolder(
				this.resourceManager.getArtifactsFolder(),
				this.resourceManager.getSelectedProjectFolder(),
				this.validatorConfiguration.getValidatedPath());

		// find resources folder
		this.projectResourcesFolder = this.commonServices.findOrCreateFolder(
				validationFolder, this.validatorConfiguration.getResourcesPath());

		// find outcomes folder
		this.projectOutcomesFolder = this.commonServices.findOrCreateFolder(
				validationFolder, this.validatorConfiguration.getOutcomesPath());

	}

	private void putResourceInValidatedFolder(File original, String filename)
			throws JobRunnerException {

		File copied = new File(this.projectResourcesFolder, filename);
		try {
			FileUtils.copyFile(original, copied);
		} catch (IOException e) {
			this.messageManager.addError(e, "Error copying file: %s", filename);

		}
	}

	private void initValidationEngine() throws JobRunnerException {

	}
}
