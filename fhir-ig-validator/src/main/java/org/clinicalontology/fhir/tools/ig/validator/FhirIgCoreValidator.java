/**
 *
 */
package org.clinicalontology.fhir.tools.ig.validator;

import org.clinicalontology.fhir.tools.ig.api.FhirIgValidatorApi;
import org.clinicalontology.fhir.tools.ig.config.CommonConfiguration;
import org.clinicalontology.fhir.tools.ig.config.ValidatorConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author dtsteven
 *
 */
@Component
public class FhirIgCoreValidator implements FhirIgValidatorApi {

	@Autowired
	private CommonConfiguration commonConfiguration;

	@Autowired
	private ValidatorConfiguration validatorConfiguration;

	@Override
	public void validate() {

		System.err.printf("FhirIgCoreValidator.validate %s %s\n",
				this.commonConfiguration.getTarget(),
				this.validatorConfiguration.getValidate());

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
