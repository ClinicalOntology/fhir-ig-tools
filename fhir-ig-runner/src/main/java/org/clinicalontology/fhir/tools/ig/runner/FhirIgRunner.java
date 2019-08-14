package org.clinicalontology.fhir.tools.ig.runner;

import org.clinicalontology.fhir.tools.ig.api.FhirIgPublisherApi;
import org.clinicalontology.fhir.tools.ig.api.FhirIgRunnerApi;
import org.clinicalontology.fhir.tools.ig.api.FhirIgValidatorApi;
import org.clinicalontology.fhir.tools.ig.config.CommonConfiguration;
import org.clinicalontology.fhir.tools.ig.config.RunnerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FhirIgRunner implements FhirIgRunnerApi {

	private boolean interruptOnError = true;
	private boolean runValidator = true;
	private boolean runPublisher = true;

	@Autowired
	private RunnerConfiguration runnerSettings;

	@Autowired
	private CommonConfiguration commonSettings;

	@Autowired
	private FhirIgValidatorApi validator;

	@Autowired
	private FhirIgPublisherApi publisher;

	@Override
	public void runJob() {

		this.validator.setInterruptOnErrorFlag(this.interruptOnError);
		// this.publisher.setInterruptOnErrorFlag(this.interruptOnError);

		if (this.runValidator) {
			this.validator.validate();
		}

		if (this.runPublisher) {
			this.publisher.publish();
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
