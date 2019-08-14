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
@ConfigurationProperties(prefix = "ig.runner")
public class RunnerConfiguration {

	private String name;
	private String description;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
