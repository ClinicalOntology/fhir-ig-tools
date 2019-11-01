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
@ConfigurationProperties(prefix = "ig.publisher")
public class PublisherConfiguration {

	public static class InterruptOnError {
		private boolean onModule;
		private boolean onResource;

		public boolean isOnModule() {
			return this.onModule;
		}

		public void setOnModule(boolean onModule) {
			this.onModule = onModule;
		}

		public boolean isOnResource() {
			return this.onResource;
		}

		public void setOnResource(boolean onResource) {
			this.onResource = onResource;
		}
	}

	public static class Paths {
		private String published;
		private String narratives;
		private String snapshots;
		private String website;

		public String getPublished() {
			return this.published;
		}

		public void setPublished(String published) {
			this.published = published;
		}

		public String getNarratives() {
			return this.narratives;
		}

		public void setNarratives(String narratives) {
			this.narratives = narratives;
		}

		public String getSnapshots() {
			return this.snapshots;
		}

		public void setSnapshots(String snapshots) {
			this.snapshots = snapshots;
		}

		public String getWebsite() {
			return this.website;
		}

		public void setWebsite(String website) {
			this.website = website;
		}

		public String getTemplates() {
			return this.templates;
		}

		public void setTemplates(String templates) {
			this.templates = templates;
		}

		public String[] getAssets() {
			return this.assets;
		}

		public void setAssets(String[] assets) {
			this.assets = assets;
		}

		private String templates;
		private String[] assets;
	}

	private InterruptOnError interruptOnError;
	private Paths paths;

	public InterruptOnError getInterruptOnError() {
		return this.interruptOnError;
	}

	public void setInterruptOnError(InterruptOnError interruptOnError) {
		this.interruptOnError = interruptOnError;
	}

	public Paths getPaths() {
		return this.paths;
	}

	public void setPaths(Paths paths) {
		this.paths = paths;
	}

	public String[] getAssetPaths() {
		return this.paths.assets;
	}

	public String getPublishedPath() {
		return this.paths.published;
	}

	public String getNarrativesPath() {
		return this.paths.narratives;
	}

	public String getSnapshotsPath() {
		return this.paths.snapshots;
	}

	public String getTemplatesPath() {
		return this.paths.templates;
	}

	public String getWebsitePath() {
		return this.paths.website;
	}

	public Boolean getInterruptIfErrorOnModule() {
		return this.interruptOnError.onModule;
	}

	public Boolean getInterruptIfErrorOnResource() {
		return this.interruptOnError.onResource;
	}

}
