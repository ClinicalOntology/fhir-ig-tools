/**
 *
 */
package org.clinicalontology.fhir.tools.ig.config;

import java.util.HashMap;
import java.util.Map;

import org.clinicalontology.fhir.tools.ig.model.FhirIgProject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author dtsteven
 *
 */
@Configuration
@ConfigurationProperties(prefix = "ig")
public class CommonConfiguration {

	public static class Paths {
		private String resources;
		private String artifacts;
		private String config;

		public String getResources() {
			return this.resources;
		}

		public void setResources(String resources) {
			this.resources = resources;
		}

		public String getArtifacts() {
			return this.artifacts;
		}

		public void setArtifacts(String artifacts) {
			this.artifacts = artifacts;
		}

		public String getConfig() {
			return this.config;
		}

		public void setConfig(String config) {
			this.config = config;
		}
	}

	private Paths paths;
	private String version;
	private String project;
	private Map<String, FhirIgProject> projects = new HashMap<>();

	public Paths getPaths() {
		return this.paths;
	}

	public void setPaths(Paths paths) {
		this.paths = paths;
	}

	public String getProject() {
		return this.project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public Map<String, FhirIgProject> getProjects() {
		return this.projects;
	}

	public void setProjects(Map<String, FhirIgProject> projects) {
		this.projects = projects;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
