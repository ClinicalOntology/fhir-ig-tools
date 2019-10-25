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

	@Autowired
	private WebsitePublisher websitePublisher;

	private ValidationSupportChain validationSupportChain;
	private FhirContext fhirContext;

	private File publishFolder;

	@Override
	public void init() throws JobRunnerException {

		this.fhirContext = this.commonServices.getFhirContext();
		this.initPublishingEngine();
		this.initFolders();

		this.snapshotPublisher.init(this.publishFolder, this.validationSupportChain);
		this.narrativePublisher.init(this.publishFolder, this.validationSupportChain);
		this.websitePublisher.init(this.publishFolder, this.validationSupportChain);

	}

	@Override
	public void publish() throws JobRunnerException {

		this.messageManager.setInterruptOnErrorFlag(this.publisherConfiguration
				.getInterruptIfErrorOnResource());

		for (String filename : this.validator.getValidatedResources()) {
			File file = this.validator.getValidatedResource(filename);

			this.snapshotPublisher.publish(file);
			this.narrativePublisher.publish(file);
			this.websitePublisher.publish(file);

			this.messageManager.addInfo("Published structure Definition: %s",
					filename);
		}

		this.websitePublisher.publish();

		if (this.publisherConfiguration.getInterruptIfErrorOnModule()) {
			this.messageManager.interruptOnError("Published");
		}

	}

	private void initFolders() throws JobRunnerException {

		// find the publish folder
		this.publishFolder = this.commonServices.findOrCreateFolder(
				this.resourceManager.getArtifactsFolder(),
				this.resourceManager.getSelectedProjectFolder(),
				this.publisherConfiguration.getPublishedPath());

	}

	private void initPublishingEngine() throws JobRunnerException {

		DefaultProfileValidationSupport defaultValidationSupport = new DefaultProfileValidationSupport();
		SnapshotGeneratingValidationSupport snapshotGenerator = new SnapshotGeneratingValidationSupport(
				this.fhirContext, defaultValidationSupport);
		this.validationSupportChain = new ValidationSupportChain(defaultValidationSupport,
				snapshotGenerator);

	}
}
