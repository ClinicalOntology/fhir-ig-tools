/**
 *
 */
package org.clinicalontology.fhir.tools.ig.validator;

import org.clinicalontology.fhir.tools.ig.api.MessageManager;
import org.clinicalontology.fhir.tools.ig.config.CommonConfiguration;
import org.clinicalontology.fhir.tools.ig.exception.JobRunnerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.context.FhirContext;

/**
 * @author dtsteven
 *
 */
@Component
public class FhirContextSelector {

	@Autowired
	private CommonConfiguration commonConfiguration;

	@Autowired
	private MessageManager messageManager;

	private FhirContext ctx;

	public void init() throws JobRunnerException {
		this.computeFhirContext();
	}

	public FhirContext GetFhirContext() {
		return this.ctx;
	}

	private void computeFhirContext() throws JobRunnerException {

		if (this.commonConfiguration.getRelease() != null) {
			switch (this.commonConfiguration.getRelease().toLowerCase()) {
			case "dstu3":
				this.ctx = FhirContext.forDstu3();
				break;
			case "r4":
				this.ctx = FhirContext.forR4();
				break;
			default:
				break;
			}

			if (this.ctx == null) {
				this.messageManager.addError(
						"Unknown value for ig.release: %s.  Must be one of 'dstu3,r4'",
						this.commonConfiguration.getRelease());
			}
		}
	}

}
