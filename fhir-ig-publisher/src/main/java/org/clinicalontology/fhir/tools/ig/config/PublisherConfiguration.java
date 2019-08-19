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
@ConfigurationProperties(prefix = "ig.publisher")
public class PublisherConfiguration {

	private Boolean interruptOnError;

	@Value("${ig.publisher.paths.published}")
	private String publishedPath;

	public String getPublishedPath() {
		return this.publishedPath;
	}

	public void setPublishedPath(String publishedPath) {
		this.publishedPath = publishedPath;
	}

	public Boolean getInterruptOnError() {
		return this.interruptOnError;
	}

	public void setInterruptOnError(Boolean interruptOnError) {
		this.interruptOnError = interruptOnError;
	}

}
