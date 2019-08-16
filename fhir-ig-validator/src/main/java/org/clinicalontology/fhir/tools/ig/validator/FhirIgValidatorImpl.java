/**
 *
 */
package org.clinicalontology.fhir.tools.ig.validator;

import java.io.File;

import org.clinicalontology.fhir.tools.ig.api.FhirIgValidator;
import org.clinicalontology.fhir.tools.ig.api.MessageManager;
import org.clinicalontology.fhir.tools.ig.common.util.FhirIgFileUtils;
import org.clinicalontology.fhir.tools.ig.config.ValidatorConfiguration;
import org.clinicalontology.fhir.tools.ig.exception.JobRunnerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author dtsteven
 *
 */
@Component
public class FhirIgValidatorImpl implements FhirIgValidator {

	@Autowired
	private MessageManager messageManager;

	@Autowired
	private FhirIgFileUtils fileUtils;

	@Autowired
	private ValidatorConfiguration validatorConfiguration;

	@Override
	public void validate() throws JobRunnerException {

		this.messageManager.setInterruptOnErrorFlag(this.validatorConfiguration
				.getInterruptOnError());

		int count = 0;
		for (File file : this.fileUtils.getSelectedProjectMembers()) {
			this.messageManager.addInfo("validate file: %s", file.getName());

			// mock error
			count++;
			if (count > 10) {
				this.messageManager.addError("Too many profiles: %s", file.getName());
			}
		}

		this.messageManager.interruptOnError("Validation");

	}

	@Override
	public boolean success() {
		// TODO Auto-generated method stub
		return false;
	}

}
