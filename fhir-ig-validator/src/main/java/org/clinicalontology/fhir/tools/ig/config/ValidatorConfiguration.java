/**
 *
 */
package org.clinicalontology.fhir.tools.ig.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author dtsteven
 *
 */
@Configuration
@ConfigurationProperties(prefix = "ig.validator")
public class ValidatorConfiguration {

	private String validate;

	public String getValidate() {
		return this.validate;
	}

	public void setValidate(String validate) {
		this.validate = validate;
	}
}
