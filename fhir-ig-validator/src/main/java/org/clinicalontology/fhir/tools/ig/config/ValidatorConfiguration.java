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

	@Value("${ig.validator.interruptOnError.onModule}")
	private Boolean interruptIfErrorOnModule;

	@Value("${ig.validator.interruptOnError.onResource}")
	private Boolean interruptIfErrorOnResource;

	@Value("${ig.validator.paths.validated}")
	private String validatedPath;

	public String getValidatedPath() {
		return this.validatedPath;
	}

	public void setValidatedPath(String validatedPath) {
		this.validatedPath = validatedPath;
	}

	public Boolean getInterruptIfErrorOnModule() {
		return this.interruptIfErrorOnModule;
	}

	public void setInterruptIfErrorOnModule(Boolean interruptIfErrorOnModule) {
		this.interruptIfErrorOnModule = interruptIfErrorOnModule;
	}

	public Boolean getInterruptIfErrorOnResource() {
		return this.interruptIfErrorOnResource;
	}

	public void setInterruptIfErrorOnResource(Boolean interruptIfErrorOnResource) {
		this.interruptIfErrorOnResource = interruptIfErrorOnResource;
	}

}
