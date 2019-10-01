/**
 *
 */
package org.clinicalontology.fhir.tools.ig.publisher;

import java.io.File;

import org.clinicalontology.fhir.tools.ig.api.FhirIgPublisher;
import org.clinicalontology.fhir.tools.ig.api.FhirIgValidator;
import org.clinicalontology.fhir.tools.ig.api.MessageManager;
import org.clinicalontology.fhir.tools.ig.common.services.FhirIgCommonServices;
import org.clinicalontology.fhir.tools.ig.common.services.FhirIgResourceManager;
import org.clinicalontology.fhir.tools.ig.config.PublisherConfiguration;
import org.clinicalontology.fhir.tools.ig.exception.JobRunnerException;
import org.hl7.fhir.r4.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.r4.hapi.validation.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.r4.hapi.validation.ValidationSupportChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.context.FhirContext;

/**
 * @author dtsteven
 *
 */
@Component
public class FhirIgPublisherImpl implements FhirIgPublisher {

	@Autowired
	private MessageManager messageManager;

	@Autowired
	private FhirIgResourceManager resourceManager;

	@Autowired
	private PublisherConfiguration publisherConfiguration;

	@Autowired
	private FhirIgValidator validator;

	@Autowired
	private FhirIgCommonServices commonServices;

	@Autowired
	private SnapshotPublisher snapshotPublisher;

	@Autowired
	private NarrativePublisher narrativePublisher;

	private ValidationSupportChain validationSupport;
	private FhirContext fhirContext;

	private File projectNarrativesFolder;
	private File projectSnapshotsFolder;

	@Override
	public void init() throws JobRunnerException {

		this.fhirContext = this.commonServices.getFhirContext();
		this.initPublishingEngine();
		this.initFolders();
	}

	@Override
	public void publish() throws JobRunnerException {

		this.commonServices.resetFolder(this.projectNarrativesFolder);
		this.commonServices.resetFolder(this.projectSnapshotsFolder);

		this.messageManager.setInterruptOnErrorFlag(this.publisherConfiguration
				.getInterruptIfErrorOnResource());

		// dummied up publish: simple copy published file to destination folder
		for (String filename : this.validator.getValidatedResources()) {
			File file = this.validator.getValidatedResource(filename);

			this.snapshotPublisher.publish(this.validationSupport, this.projectSnapshotsFolder,
					file);
			this.narrativePublisher.publish(this.validationSupport, this.projectNarrativesFolder,
					file);
			this.messageManager.addInfo("Published structure Definition: %s",
					filename);
		}

		if (this.publisherConfiguration.getInterruptIfErrorOnModule()) {
			this.messageManager.interruptOnError("Published");
		}

	}

	private void initFolders() throws JobRunnerException {

		// find the publish folder
		File publishFolder = this.commonServices.findOrCreateFolder(
				this.resourceManager.getArtifactsFolder(),
				this.resourceManager.getSelectedProjectFolder(),
				this.publisherConfiguration.getPublishedPath());

		// find narratives folder
		this.projectNarrativesFolder = this.commonServices.findOrCreateFolder(
				publishFolder, this.publisherConfiguration.getNarrativesPath());

		// find snapshots folder
		this.projectSnapshotsFolder = this.commonServices.findOrCreateFolder(
				publishFolder, this.publisherConfiguration.getSnapshotsPath());

	}

	private void initPublishingEngine() throws JobRunnerException {

		DefaultProfileValidationSupport defaultValidationSupport = new DefaultProfileValidationSupport();
		SnapshotGeneratingValidationSupport snapshotGenerator = new SnapshotGeneratingValidationSupport(
				this.fhirContext, defaultValidationSupport);
		this.validationSupport = new ValidationSupportChain(defaultValidationSupport,
				snapshotGenerator);

	}
}
