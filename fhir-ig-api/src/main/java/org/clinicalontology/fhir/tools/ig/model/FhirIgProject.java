/**
 *
 */
package org.clinicalontology.fhir.tools.ig.model;

/**
 * @author dtsteven
 *
 *         POJO to describe a FHIR IG project.
 */
public class FhirIgProject {

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
