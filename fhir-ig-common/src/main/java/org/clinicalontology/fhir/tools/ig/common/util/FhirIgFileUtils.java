package org.clinicalontology.fhir.tools.ig.common.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.clinicalontology.fhir.tools.ig.api.MessageManager;
import org.clinicalontology.fhir.tools.ig.config.CommonConfiguration;
import org.clinicalontology.fhir.tools.ig.config.CommonConfiguration.Project;
import org.clinicalontology.fhir.tools.ig.exception.JobRunnerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FhirIgFileUtils {

	@Autowired
	private CommonConfiguration configuration;

	@Autowired
	private MessageManager messageManager;

	private Project selectedProject;
	private File resourcesFolder;

	@PostConstruct
	public void init() throws JobRunnerException {
		this.selectedProject = this.configuration.getProjects().get(this.configuration
				.getProject());
		if (this.selectedProject == null) {
			this.messageManager.addError(
					"Invalid ig.package: %s.  Does not match a package in the packages list",
					this.configuration.getProject());
		}

		this.resourcesFolder = new File(this.configuration.getPaths().getResources());
		if (!this.resourcesFolder.exists()) {
			this.messageManager.addError(
					"Invalid ig.path.resources: %s.  Does not exist",
					this.configuration.getPaths().getResources());
		} else if (!this.resourcesFolder.isDirectory()) {
			this.messageManager.addError(
					"Invalid ig.path.resources: %s.  Is not a folder",
					this.configuration.getPaths().getResources());

		}
	}

	public Project getSelectedProject() {
		return this.selectedProject;
	}

	public String getSelectedProjectName() {

		return this.selectedProject.getName();
	}

	public List<File> getSelectedProjectMembers() throws JobRunnerException {

		List<File> files = new ArrayList<>();
		File dir = new File(this.resourcesFolder, this.selectedProject.getPath());
		if (!dir.exists()) {
			this.messageManager.addError("%s does not exist", dir.getPath());
		} else if (!dir.isDirectory()) {
			this.messageManager.addError("%s is not a folder", dir.getPath());
		} else {

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
					files.add(file);
				}
			}
		}
		return files;
	}
}
