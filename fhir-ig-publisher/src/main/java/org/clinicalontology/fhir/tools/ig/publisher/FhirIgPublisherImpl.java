/**
 *
 */
package org.clinicalontology.fhir.tools.ig.publisher;

import org.clinicalontology.fhir.tools.ig.api.FhirIgPublisher;
import org.clinicalontology.fhir.tools.ig.api.FhirIgValidator;
import org.clinicalontology.fhir.tools.ig.api.MessageManager;
import org.clinicalontology.fhir.tools.ig.config.PublisherConfiguration;
import org.clinicalontology.fhir.tools.ig.exception.JobRunnerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author dtsteven
 *
 */
@Component
public class FhirIgPublisherImpl implements FhirIgPublisher {

	@Autowired
	private MessageManager messageManager;

	@Autowired
	private PublisherConfiguration publisherConfiguration;

	@Autowired
	private FhirIgValidator validator;

	@Override
	public void init() throws JobRunnerException {

	}

	@Override
	public void publish() throws JobRunnerException {

		this.messageManager.setInterruptOnErrorFlag(this.publisherConfiguration
				.getInterruptOnError());

		// dummied up validation: simple copy validated file to destination folder
		for (String filename : this.validator.getValidatedResources()) {
			this.messageManager.addInfo("Published structure Definition: %s",
					filename);
		}

		this.messageManager.interruptOnError("Published");

	}

}
