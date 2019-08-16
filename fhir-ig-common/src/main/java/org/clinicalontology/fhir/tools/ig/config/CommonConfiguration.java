/**
 *
 */
package org.clinicalontology.fhir.tools.ig.config;

import java.util.HashMap;
import java.util.Map;

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

	public static class Project {
		private String name;
		private String path;
		private String filter;

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPath() {
			return this.path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public String getFilter() {
			return this.filter;
		}

		public void setFilter(String filter) {
			this.filter = filter;
		}
	}

	private Paths paths;

	private String project;

	public String getProject() {
		return this.project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	private Map<String, Project> projects = new HashMap<>();

	public Paths getPaths() {
		return this.paths;
	}

	public void setPaths(Paths paths) {
		this.paths = paths;
	}

	public Map<String, Project> getProjects() {
		return this.projects;
	}

	public void setProjects(Map<String, Project> projects) {
		this.projects = projects;
	}

}
