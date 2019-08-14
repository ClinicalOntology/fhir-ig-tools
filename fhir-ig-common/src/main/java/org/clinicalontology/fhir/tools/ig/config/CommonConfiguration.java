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
@ConfigurationProperties(prefix = "ig.common")
public class CommonConfiguration {

	private String target;

	public String getTarget() {
		return this.target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
}
