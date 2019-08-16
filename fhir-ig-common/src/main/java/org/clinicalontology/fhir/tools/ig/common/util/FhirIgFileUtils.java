package org.clinicalontology.fhir.tools.ig.common.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.clinicalontology.fhir.tools.ig.api.MessageManagerApi;
import org.clinicalontology.fhir.tools.ig.config.CommonConfiguration;
import org.clinicalontology.fhir.tools.ig.config.CommonConfiguration.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FhirIgFileUtils {

	@Autowired
	private CommonConfiguration configuration;

	@Autowired
	private MessageManagerApi messageManager;

	private Package selectedPackage;
	private File resourcesFolder;

	@PostConstruct
	public void init() {
		this.selectedPackage = this.configuration.getPackages().get(this.configuration
				.getPackage());
		if (this.selectedPackage == null) {
			this.messageManager.addError(
					"Invalid ig.package: %s.  Does not match a package in the packages list",
					this.configuration.getPackage());
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

	public Package getSelectedPackage() {
		return this.selectedPackage;
	}

	public String getSelectedPackageName() {

		return this.selectedPackage.getName();
	}

	public List<File> getSelectedPackageMembers() {

		List<File> files = new ArrayList<>();
		File dir = new File(this.resourcesFolder, this.selectedPackage.getPath());
		if (!dir.exists()) {
			this.messageManager.addError("%s does not exist", dir.getPath());
		} else if (!dir.isDirectory()) {
			this.messageManager.addError("%s is not a folder", dir.getPath());
		} else {

			String[] fileList;
			if (this.selectedPackage.getFilter() == null) {
				fileList = dir.list();
			} else {
				FilenameFilter fileFilter = new WildcardFileFilter(this.selectedPackage
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
