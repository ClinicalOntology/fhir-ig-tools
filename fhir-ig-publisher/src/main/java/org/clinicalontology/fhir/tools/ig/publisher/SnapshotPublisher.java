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
import org.clinicalontology.fhir.tools.ig.config.PublisherConfiguration;
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

	@Autowired
	private PublisherConfiguration publisherConfiguration;

	private File projectSnapshotsFolder;
	private ValidationSupportChain validationSupportChain;

	public void init(File publishFolder,
			ValidationSupportChain validationSupportChain) throws JobRunnerException {

		// find snapshots folder
		this.projectSnapshotsFolder = this.commonServices.findOrCreateFolder(
				publishFolder, this.publisherConfiguration.getSnapshotsPath());
		this.commonServices.resetFolder(this.projectSnapshotsFolder);
		this.validationSupportChain = validationSupportChain;
	}

	public void publish(File profile) throws JobRunnerException {

		try {
			StructureDefinition sd = this.commonServices.getXmlParser().parseResource(
					StructureDefinition.class, new FileReader(profile));

			this.validationSupportChain.generateSnapshot(sd, "http://ihc.hdd", null, "MyProfile");

			String sdString = this.commonServices
					.getXmlParser()
					.setPrettyPrint(true)
					.encodeResourceToString(sd);

			this.putResourceInSnapshotFolder(sdString, profile.getName(), this.projectSnapshotsFolder);
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
