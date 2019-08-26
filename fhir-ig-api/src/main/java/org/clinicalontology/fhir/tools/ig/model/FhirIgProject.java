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
	private String folder;
	private String filter;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFolder() {
		return this.folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getFilter() {
		return this.filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}
}
