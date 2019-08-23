/**
 *
 */
package org.clinicalontology.fhir.tools.ig.config;

import org.springframework.beans.factory.annotation.Value;
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

	@Value("${ig.validator.paths.validated}")
	private String validatedPath;

	public String getValidatedPath() {
		return this.validatedPath;
	}

	public void setValidatedPath(String validatedPath) {
		this.validatedPath = validatedPath;
	}

	public Boolean getInterruptOnError() {
		return this.interruptOnError;
	}

	public void setInterruptOnError(Boolean interruptOnError) {
		this.interruptOnError = interruptOnError;
	}

}
