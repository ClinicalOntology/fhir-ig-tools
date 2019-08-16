package org.clinicalontology.fhir.tools.ig.runner;

import org.clinicalontology.fhir.tools.ig.api.FhirIgPublisherApi;
import org.clinicalontology.fhir.tools.ig.api.FhirIgRunnerApi;
import org.clinicalontology.fhir.tools.ig.api.FhirIgValidatorApi;
import org.clinicalontology.fhir.tools.ig.api.MessageManagerApi;
import org.clinicalontology.fhir.tools.ig.exception.JobRunnerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FhirIgRunner implements FhirIgRunnerApi {

	private boolean interruptOnError = true;
	private boolean runValidator = true;
	private boolean runPublisher = true;

	@Autowired
	private MessageManagerApi messageManager;

	@Autowired
	private FhirIgValidatorApi validator;

	@Autowired
	private FhirIgPublisherApi publisher;

	@Override
	public void runJob() {

		try {

			this.validator.setInterruptOnErrorFlag(this.interruptOnError);

			this.messageManager.addInfo("Starting");

			if (this.runValidator) {
				this.validator.validate();
			}

			if (this.runPublisher) {
				this.publisher.publish();
			}
			this.messageManager.addInfo("Finished");

		} catch (JobRunnerException e) {
			this.messageManager.addError(e);
		}

	}

	@Override
	public boolean interruptOnError() {
		return this.interruptOnError;
	}

	@Override
	public void setInterruptOnErrorFlag(boolean interruptOnError) {
		this.interruptOnError = interruptOnError;
	}

	@Override
	public void processCommandArgs(String[] args) {
		if (args != null && args.length > 0) {
			this.runPublisher = false;
			this.runValidator = false;

			for (String arg : args) {
				switch (arg.toLowerCase()) {
				case "validate":
					this.runValidator = true;
					break;
				case "publish":
					this.runPublisher = true;
					break;
				default:
					// TODO throw error here
					break;
				}
			}
		}
	}
}
