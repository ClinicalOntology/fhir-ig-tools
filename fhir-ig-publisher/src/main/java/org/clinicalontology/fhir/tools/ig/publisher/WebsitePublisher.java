/**
 *
 */
package org.clinicalontology.fhir.tools.ig.publisher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.clinicalontology.fhir.tools.ig.api.MessageManager;
import org.clinicalontology.fhir.tools.ig.common.services.FhirIgCommonServices;
import org.clinicalontology.fhir.tools.ig.common.services.FhirIgResourceManager;
import org.clinicalontology.fhir.tools.ig.config.PublisherConfiguration;
import org.clinicalontology.fhir.tools.ig.exception.JobRunnerException;
import org.hl7.fhir.r4.hapi.validation.ValidationSupportChain;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author dtsteven
 *
 */
@Component
public class WebsitePublisher {

	@Autowired
	private MessageManager messageManager;

	@Autowired
	private PublisherConfiguration publisherConfiguration;

	@Autowired
	private FhirIgCommonServices commonServices;

	@Autowired
	private FhirIgResourceManager resourceManager;

	@Autowired
	private ZipfilePublisher zipfilePublisher;

	private Configuration cfg;
	private File projectTemplatesFolder;
	private File templatesFolder;
	private File websiteFolder;
	private ValidationSupportChain validationSupportChain;
	private Map<String, Object> websiteModel;
	private List<Map<String, Object>> profiles;

	public void init(File publishFolder,
			ValidationSupportChain validationSupportChain) throws JobRunnerException {

		this.websiteModel = new HashMap<String, Object>();
		this.profiles = new ArrayList<>();
		this.websiteModel.put("profiles", this.profiles);
		this.websiteModel.put("projectName", this.resourceManager.getSelectedProjectName());

		this.validationSupportChain = validationSupportChain;

		this.initFolders(publishFolder);

		this.initConfiguration();

	}

	/**
	 * publish all the site specific files
	 *
	 * @throws JobRunnerException
	 */
	public void publish() throws JobRunnerException {

		this.createIndexHtml();
		this.moveAssetsToWebsite();

		this.zipfilePublisher.publish(this.websiteFolder);
	}

	/**
	 * publish a single file for structure definition
	 *
	 * @throws JobRunnerException
	 */
	public void publish(File file) throws JobRunnerException {
		File fileHtml = new File(this.websiteFolder, file.getName() + ".html");

		Map<String, Object> model = this.generateProfileModel(file, fileHtml);
		this.profiles.add(model);

		try (FileOutputStream fos = new FileOutputStream(fileHtml)) {
			Template template = this.cfg.getTemplate("profile.ftl");

			Writer osw = new OutputStreamWriter(fos);
			template.process(model, osw);

		} catch (IOException | TemplateException e) {
			this.messageManager.addError(e, "Publishing %s", file.getName());
		}
	}

	private Map<String, Object> generateProfileModel(File file, File fileHtml) throws JobRunnerException {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			StructureDefinition sd = this.commonServices.getXmlParser()
					.parseResource(StructureDefinition.class, new FileReader(file));
			this.validationSupportChain.generateSnapshot(sd, "http://ihc.hdd", null, "MyProfile");

			model.put("name", sd.getName());
			model.put("link", fileHtml.getName());
			List<Map<String, Object>> elements = new ArrayList<Map<String, Object>>();
			model.put("elements", elements);

			for (ElementDefinition element : sd.getDifferential().getElement()) {
				Map<String, Object> elementModel = new HashMap<String, Object>();
				elementModel.put("path", element.getPath());
				elements.add(elementModel);
			}

		} catch (FileNotFoundException e) {
			this.messageManager.addError(e, "Reading %s", file.getName());
		}
		return model;
	}

	private void createIndexHtml() throws JobRunnerException {
		File indexHtml = new File(this.websiteFolder, "index.html");

		try (FileOutputStream fos = new FileOutputStream(indexHtml)) {
			Template template = this.cfg.getTemplate("index.ftl");

			Writer osw = new OutputStreamWriter(fos);
			template.process(this.websiteModel, osw);

		} catch (TemplateException | IOException e) {
			this.messageManager.addError(e, "Publishing Website");
			e.printStackTrace();
		}

	}

	private static final String[] assets = new String[] {
			"bootstrap.min.css", "bootstrap.min.js", "jquery.min.js", "popper.min.js"
	};

	private void moveAssetsToWebsite() throws JobRunnerException {
		File assetsFolder = this.commonServices.findOrCreateFolder(
				this.websiteFolder, "assets");
		this.commonServices.resetFolder(assetsFolder);

		for (String asset : assets) {
			try (InputStream is = this.getClass().getResourceAsStream("/assets/" + asset)) {
				File output = new File(assetsFolder, asset);
				FileUtils.copyInputStreamToFile(is, output);
			} catch (IOException e) {
				this.messageManager.addError(e, "Copying assets: " + asset);
			}
		}
	}

	private void initConfiguration() throws JobRunnerException {

		this.cfg = new Configuration(Configuration.VERSION_2_3_29);
		this.cfg.setDefaultEncoding("UTF-8");

		List<TemplateLoader> tl = new ArrayList<>();
		try {
			if (this.projectTemplatesFolder.exists() && this.projectTemplatesFolder.isDirectory()) {
				tl.add(new FileTemplateLoader(this.projectTemplatesFolder));
			}
			if (this.templatesFolder.exists() && this.templatesFolder.isDirectory()) {
				tl.add(new FileTemplateLoader(this.templatesFolder));
			}
		} catch (IOException e) {
			throw new JobRunnerException("Calling HtmlPublisher.initConfiguration", e);
		}
		tl.add(new ClassTemplateLoader(WebsitePublisher.class, "/templates"));
		tl.toArray(new TemplateLoader[tl.size()]);
		MultiTemplateLoader mtl = new MultiTemplateLoader(tl.toArray(new TemplateLoader[tl.size()]));

		this.cfg.setTemplateLoader(mtl);

	}

	private void initFolders(File publishFolder) throws JobRunnerException {

		this.templatesFolder = new File(this.resourceManager.getResourcesFolder(),
				this.publisherConfiguration.getTemplatesPath());

		this.projectTemplatesFolder = new File(this.resourceManager.findSelectedProjectResourceFolder(),
				this.publisherConfiguration.getTemplatesPath());

		this.websiteFolder = this.commonServices.findOrCreateFolder(
				publishFolder, this.publisherConfiguration.getWebsitePath());

	}
}
