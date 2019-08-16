/**
 *
 */
package org.clinicalontology.fhir.tools.ig.validator;

import java.io.File;

import org.clinicalontology.fhir.tools.ig.api.FhirIgValidatorApi;
import org.clinicalontology.fhir.tools.ig.api.MessageManagerApi;
import org.clinicalontology.fhir.tools.ig.common.util.FhirIgFileUtils;
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
	private MessageManagerApi messageManager;

	@Autowired
	private CommonConfiguration commonConfiguration;

	@Autowired
	private FhirIgFileUtils fileUtils;

	@Autowired
	private ValidatorConfiguration validatorConfiguration;

	@Override
	public void validate() {

		for (File file : this.fileUtils.getSelectedPackageMembers()) {
			System.err.printf("validate files: %s\n", file.getName());
		}

		this.messageManager.addInfo("FhirIgCoreValidator.validate %s %s\n",
				this.commonConfiguration.getPackage(),
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
