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

	@Value("${ig.publisher.interruptOnError.onModule}")
	private Boolean interruptIfErrorOnModule;

	@Value("${ig.publisher.interruptOnError.onResource}")
	private Boolean interruptIfErrorOnResource;

	@Value("${ig.publisher.paths.published}")
	private String publishedPath;

	@Value("${ig.publisher.paths.narratives}")
	private String narrativesPath;

	@Value("${ig.publisher.paths.snapshots}")
	private String snapshotsPath;

	public String getPublishedPath() {
		return this.publishedPath;
	}

	public String getNarrativesPath() {
		return this.narrativesPath;
	}

	public String getSnapshotsPath() {
		return this.snapshotsPath;
	}

	public void setPublishedPath(String publishedPath) {
		this.publishedPath = publishedPath;
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
