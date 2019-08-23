/**
 *
 */
package org.clinicalontology.fhir.tools.ig.api;

import java.io.File;
import java.util.List;

import org.clinicalontology.fhir.tools.ig.exception.JobRunnerException;

/**
 * @author dtsteven
 *
 *         Manager to handle access to project resources
 *
 */
public interface ResourceManager {

	public String getSelectedProjectName();

	public List<String> getSelectedProjectMembers() throws JobRunnerException;

	public File getSelectedProjectMember(String modelName) throws JobRunnerException;

	public void init() throws JobRunnerException;
}
