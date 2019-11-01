/**
 *
 */
package org.clinicalontology.fhir.tools.ig.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionDifferentialComponent;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionSnapshotComponent;
import org.hl7.fhir.r4.model.StructureDefinition.TypeDerivationRule;

import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateModelException;

/**
 * @author dtsteven
 *
 *         class containing all the info of the SD in format usable by
 *         freemarker
 */

public class FigProfileModel {

	private ObjectWrapper wrapper;

	public FigProfileModel(ObjectWrapper wrapper) {
		this.wrapper = wrapper;
	}

	public Map<String, Object> process(StructureDefinition sd) throws TemplateModelException {

		// this.sd = sd;
		Map<String, Object> model = new HashMap<>();

		model.put("name", this.getNameFromUrl(sd.getUrl()));
		model.put("baseName", this.getNameFromUrl(sd.getBaseDefinition()));
		model.put("derivation", sd.getDerivation() != null ? sd.getDerivation() : TypeDerivationRule.NULL);
		model.put("abstract", sd.getAbstract());

		if (sd.hasSnapshot()) {
			ElementNode snapshotTree = new ElementNode(this.wrapper);
			StructureDefinitionSnapshotComponent snapshot = sd.getSnapshot();
			this.createElementTree(snapshot.getElement(), snapshotTree);
			model.put("snapshot", snapshotTree);
		}
		if (sd.hasDifferential()) {
			ElementNode differentialTree = new ElementNode(this.wrapper);
			StructureDefinitionDifferentialComponent differential = sd
					.getDifferential();
			this.createElementTree(differential.getElement(), differentialTree);
			model.put("differential", differentialTree);
		}

		return model;
	}

	private String[] split(String key) {
		return StringUtils.split(key, '.');
	}

	private String getNameFromUrl(String url) {
		return (url != null) ? StringUtils.substringAfterLast(url, "/") : null;
	}

	public String getNameFromPath(String path) {
		String result = StringUtils.substringAfterLast(path, ".");
		if (result == null || result.isEmpty()) {
			result = path;
		}
		return result;

	}

	private void createElementTree(List<ElementDefinition> elements,
			ElementNode tree) throws TemplateModelException {
		for (ElementDefinition element : elements) {
			String[] keys = this.split(element.getPath());
			tree.add(keys, element);
		}
	}
}
