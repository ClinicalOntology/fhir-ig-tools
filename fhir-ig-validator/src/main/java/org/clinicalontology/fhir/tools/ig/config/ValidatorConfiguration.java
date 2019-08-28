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

	@Value("${ig.validator.validate.onSchema}")
	private Boolean validateOnSchema;

	@Value("${ig.validator.validate.onSchematron}")
	private Boolean validateOnSchematron;

	@Value("${ig.validator.paths.validated}")
	private String validatedPath;

	public Boolean getValidateOnSchema() {
		return this.validateOnSchema;
	}

	public void setValidateOnSchema(Boolean validateOnSchema) {
		this.validateOnSchema = validateOnSchema;
	}

	public Boolean getValidateOnSchematron() {
		return this.validateOnSchematron;
	}

	public void setValidateOnSchematron(Boolean validateOnSchematron) {
		this.validateOnSchematron = validateOnSchematron;
	}

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
