package org.clinicalontology.fhir.tools.ig.validator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.clinicalontology.fhir.tools.ig.api.MessageManager;
import org.clinicalontology.fhir.tools.ig.common.services.FhirIgCommonServices;
import org.clinicalontology.fhir.tools.ig.exception.JobRunnerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.uhn.fhir.validation.ValidationResult;

@Service
public class OperationOutcomePublisher {

	@Autowired
	private FhirIgCommonServices commonServices;

	@Autowired
	private MessageManager messageManager;

	public void publish(File projectOutcomesFolder, File original, ValidationResult result)
			throws JobRunnerException {

		try {
			String outcome = this.commonServices.getXmlParser()
					.setPrettyPrint(true)
					.encodeResourceToString(result.toOperationOutcome());

			this.putResultInOutcomesFolder(projectOutcomesFolder, original.getName(), outcome);
		} catch (IOException e) {
			this.messageManager.addError(e, "Error writing file: %s: %s", original.getName(), e
					.getLocalizedMessage());
		}
	}

	private void putResultInOutcomesFolder(File snapshotFolder, String filename, String sdString)
			throws IOException {

		File copied = new File(snapshotFolder, filename);

		FileUtils.writeStringToFile(copied, sdString, Charset.defaultCharset());

	}
}
