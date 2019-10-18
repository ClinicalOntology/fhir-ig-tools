/**
 *
 */
package org.clinicalontology.fhir.tools.ig.publisher;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.clinicalontology.fhir.tools.ig.api.MessageManager;
import org.clinicalontology.fhir.tools.ig.common.services.FhirIgCommonServices;
import org.clinicalontology.fhir.tools.ig.exception.JobRunnerException;
import org.hl7.fhir.r4.hapi.validation.ValidationSupportChain;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dtsteven
 *
 */
@Service
public class SnapshotPublisher {

	@Autowired
	private FhirIgCommonServices commonServices;

	@Autowired
	private MessageManager messageManager;

	public void publish(ValidationSupportChain validationSupport, File snapshotFolder,
			File profile) throws JobRunnerException {

		try {
			StructureDefinition sd = this.commonServices.getXmlParser().parseResource(
					StructureDefinition.class,
					new FileReader(profile));

			validationSupport.generateSnapshot(sd, "http://ihc.hdd", null, "MyProfile");

			String sdString = this.commonServices.getXmlParser().setPrettyPrint(true)
					.encodeResourceToString(
							sd);

			this.putResourceInSnapshotFolder(sdString, profile.getName(), snapshotFolder);
		} catch (IOException e) {
			this.messageManager.addError(e, "Error writing file: %s: %s", profile.getName(), e
					.getLocalizedMessage());
		}
	}

	private void putResourceInSnapshotFolder(String sdString, String filename,
			File snapshotFolder) throws IOException {

		File copied = new File(snapshotFolder, filename);

		FileUtils.writeStringToFile(copied, sdString, Charset.defaultCharset());

	}
}
