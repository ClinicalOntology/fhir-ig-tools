package org.clinicalontology.fhir.tools.ig.runner;

import org.clinicalontology.fhir.tools.ig.api.FhirIgPublisher;
import org.clinicalontology.fhir.tools.ig.api.FhirIgRunner;
import org.clinicalontology.fhir.tools.ig.api.FhirIgValidator;
import org.clinicalontology.fhir.tools.ig.api.MessageManager;
import org.clinicalontology.fhir.tools.ig.api.ResourceManager;
import org.clinicalontology.fhir.tools.ig.exception.JobRunnerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FhirIgRunnerImpl implements FhirIgRunner {

	private boolean interruptOnError = true;
	private boolean runValidator = true;
	private boolean runPublisher = true;

	@Autowired
	private ResourceManager resourceManager;

	@Autowired
	private MessageManager messageManager;

	@Autowired
	private FhirIgValidator validator;

	@Autowired
	private FhirIgPublisher publisher;

	@Override
	public void runJob() {

		try {

			// do the initialization here so we can capture exceptions cleanly
			this.messageManager.init();
			this.resourceManager.init();
			this.validator.init();
			this.publisher.init();

			this.messageManager.addInfo("Started");

			if (this.runValidator) {
				this.validator.validate();
			}

			if (this.runPublisher) {
				this.publisher.publish();
			}
			this.messageManager.addInfo("Finished");

		} catch (JobRunnerException e) {
			this.messageManager.addInfo("Interrupted: %s", e.getLocalizedMessage());

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
