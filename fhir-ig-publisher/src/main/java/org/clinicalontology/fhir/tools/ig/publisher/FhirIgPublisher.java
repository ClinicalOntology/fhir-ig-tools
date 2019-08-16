/**
 *
 */
package org.clinicalontology.fhir.tools.ig.publisher;

import org.clinicalontology.fhir.tools.ig.api.FhirIgPublisherApi;
import org.clinicalontology.fhir.tools.ig.api.MessageManagerApi;
import org.clinicalontology.fhir.tools.ig.config.PublisherConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author dtsteven
 *
 */
@Component
public class FhirIgPublisher implements FhirIgPublisherApi {

	@Autowired
	private MessageManagerApi messageManager;

	@Autowired
	private PublisherConfiguration publisherConfiguration;

	@Override
	public void publish() {

		this.messageManager.addInfo("FhirIgCorePublisher.publish %s\n",
				this.publisherConfiguration.getTarget());

	}

	@Override
	public boolean success() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean interruptOnError() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setInterruptOnErrorFlag(boolean interruptOnError) {
		// TODO Auto-generated method stub

	}

}
