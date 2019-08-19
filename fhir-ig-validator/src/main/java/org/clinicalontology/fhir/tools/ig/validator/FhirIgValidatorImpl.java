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
import org.clinicalontology.fhir.tools.ig.config.ValidatorConfiguration;
import org.clinicalontology.fhir.tools.ig.exception.JobRunnerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

	private File validatedFolder;

	@Override
	public void init() throws JobRunnerException {
		this.validatedFolder = new File(this.resourceManager.getArtifactsFolder(),
				this.validatorConfiguration.getValidatedPath());
		if (!this.validatedFolder.exists()) {
			this.messageManager.addFatalError("%s does not exist",
					this.validatedFolder.getPath());
		}

		if (!this.validatedFolder.isDirectory()) {
			this.messageManager.addFatalError("%s is not a folder",
					this.validatedFolder.getPath());
		}

		// delete the contents of the destination directory
		try {
			FileUtils.cleanDirectory(this.validatedFolder);
		} catch (IOException e) {
			this.messageManager.addFatalError("Failure to clear %s", this.validatedFolder
					.getName());
		}
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
}
