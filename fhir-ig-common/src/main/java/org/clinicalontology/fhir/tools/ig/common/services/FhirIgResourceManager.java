package org.clinicalontology.fhir.tools.ig.common.services;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.clinicalontology.fhir.tools.ig.api.MessageManager;
import org.clinicalontology.fhir.tools.ig.api.ResourceManager;
import org.clinicalontology.fhir.tools.ig.config.CommonConfiguration;
import org.clinicalontology.fhir.tools.ig.exception.JobRunnerException;
import org.clinicalontology.fhir.tools.ig.model.FhirIgProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FhirIgResourceManager implements ResourceManager {

	@Autowired
	private CommonConfiguration configuration;

	@Autowired
	private MessageManager messageManager;

	private FhirIgProject selectedProject;
	private File resourcesFolder;
	private File artifactsFolder;

	@Override
	public void init() throws JobRunnerException {
		this.selectedProject = this.configuration.getProjects().get(this.configuration
				.getProject());
		if (this.selectedProject == null) {
			this.messageManager.addError(
					"Invalid ig.package: %s.  Does not match a package in the packages list",
					this.configuration.getProject());
		}

		this.resourcesFolder = this.getResourceFolder(this.configuration.getPaths()
				.getResources(), "ig.path.resources", false);
		this.artifactsFolder = this.getResourceFolder(this.configuration.getPaths()
				.getArtifacts(), "ig.path.artifacts", true);

	}

	@Override
	public String getSelectedProjectName() {

		return this.selectedProject.getName();
	}

	@Override
	public String getSelectedProjectFolder() {
		return this.selectedProject.getFolder();
	}

	@Override
	public List<String> getSelectedProjectMembers() throws JobRunnerException {

		List<String> files = new ArrayList<>();
		File dir = this.getSelectedProjectResourceFolder();

		String[] fileList;
		if (this.selectedProject.getFilter() == null) {
			fileList = dir.list();
		} else {
			FilenameFilter fileFilter = new WildcardFileFilter(this.selectedProject
					.getFilter());
			fileList = dir.list(fileFilter);
		}
		for (String filename : fileList) {
			File file = new File(dir, filename);
			if (file.isFile()) {
				files.add(filename);
			}
		}

		return files;
	}

	@Override
	public File getSelectedProjectMember(String filename) throws JobRunnerException {

		File dir = this.getSelectedProjectResourceFolder();
		File file = new File(dir, filename);
		if (file.exists() && file.isFile()) {
			return file;
		} else {
			return null;
		}

	}

	private File getSelectedProjectResourceFolder() throws JobRunnerException {

		File dir = new File(this.resourcesFolder, this.selectedProject.getFolder());
		if (!dir.exists()) {
			this.messageManager.addFatalError("%s does not exist", dir.getPath());
		}

		if (!dir.isDirectory()) {
			this.messageManager.addFatalError("%s is not a folder", dir.getPath());
		}

		return dir;
	}

	private File getResourceFolder(String resourceFolder, String configPath,
			boolean create)
			throws JobRunnerException {
		File folder = new File(resourceFolder);
		if (!folder.exists()) {
			if (create) {
				folder.mkdir();
				this.messageManager.addInfo("Created %s", folder.getPath());
			} else {
				this.messageManager.addFatalError(
						"Invalid %s: %s.  Does not exist",
						configPath,
						this.configuration.getPaths().getResources());
			}

		} else if (!folder.isDirectory()) {
			this.messageManager.addFatalError(
					"Invalid %s: %s.  Is not a folder",
					configPath,
					this.configuration.getPaths().getResources());

		}

		return folder;
	}

	public File getArtifactsFolder() {
		return this.artifactsFolder;
	}

}
