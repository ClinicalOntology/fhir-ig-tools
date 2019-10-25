/**
 *
 */
package org.clinicalontology.fhir.tools.ig.publisher;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.clinicalontology.fhir.tools.ig.api.MessageManager;
import org.clinicalontology.fhir.tools.ig.common.services.FhirIgCommonServices;
import org.clinicalontology.fhir.tools.ig.config.PublisherConfiguration;
import org.clinicalontology.fhir.tools.ig.exception.JobRunnerException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.INarrative;
import org.hl7.fhir.r4.hapi.validation.ValidationSupportChain;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.INarrativeGenerator;

/**
 * @author dtsteven
 *
 */
@Service
public class NarrativePublisher {
	@Autowired
	private FhirIgCommonServices commonServices;

	@Autowired
	private MessageManager messageManager;

	@Autowired
	private PublisherConfiguration publisherConfiguration;

	private File projectNarrativesFolder;
	private ValidationSupportChain validationSupportChain;

	public void init(File publishFolder,
			ValidationSupportChain validationSupportChain) throws JobRunnerException {
		// find narratives folder
		this.projectNarrativesFolder = this.commonServices.findOrCreateFolder(
				publishFolder, this.publisherConfiguration.getNarrativesPath());
		this.validationSupportChain = validationSupportChain;
		this.commonServices.resetFolder(this.projectNarrativesFolder);
	}

	public void publish(File profile) throws JobRunnerException {

		try {

			INarrativeGenerator gen = new INarrativeGenerator() {

				@Override
				public boolean populateResourceNarrative(FhirContext theFhirContext,
						IBaseResource theResource) {

					if (theResource instanceof StructureDefinition) {
						StructureDefinition sd = (StructureDefinition) theResource;
						Base base = sd.addChild("text");
						INarrative theNarrative = sd.castToNarrative(base);
						try {
							theNarrative.setDivAsString("<div>narrative text</div>");
						} catch (Exception e) {
							throw new Error(e);
						}
						theNarrative.setStatusAsString("generated");
						return true;

					}
					return false;
				}
			};

			this.commonServices.setNarrativeGenerator(gen);
			StructureDefinition sd = this.commonServices.getXmlParser()
					.parseResource(StructureDefinition.class, new FileReader(profile));

			this.validationSupportChain.generateSnapshot(sd, "http://ihc.hdd", null, "MyProfile");

			String sdString = this.commonServices.getFhirContext()
					.newXmlParser()
					.setPrettyPrint(true)
					.encodeResourceToString(sd);

			this.putResourceInNarrativeFolder(sdString, profile.getName(), this.projectNarrativesFolder);
		} catch (IOException e) {
			this.messageManager.addError(e, "Error writing file: %s: %s", profile.getName(), e
					.getLocalizedMessage());
		} finally {
			this.commonServices.resetNarrativeGenerator();
		}
	}

	private void putResourceInNarrativeFolder(String sdString, String filename,
			File snapshotFolder) throws IOException {

		File copied = new File(snapshotFolder, filename);

		FileUtils.writeStringToFile(copied, sdString, Charset.defaultCharset());

	}
}
