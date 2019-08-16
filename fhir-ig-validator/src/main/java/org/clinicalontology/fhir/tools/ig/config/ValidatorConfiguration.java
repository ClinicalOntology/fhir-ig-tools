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

	private Boolean interruptOnError;

	public Boolean getInterruptOnError() {
		return this.interruptOnError;
	}

	public void setInterruptOnError(Boolean interruptOnError) {
		this.interruptOnError = interruptOnError;
	}

}
