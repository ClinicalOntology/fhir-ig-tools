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

/**
 * @author dtsteven
 *
 *         class containing all the info of the SD with addition elements to
 *         assist in building the cem.
 */
public class FigProfileModel {
	private final StructureDefinition sd;
	private final Map<String, Object> model = new HashMap<>();
	private final FigTreeNode<String, Map<String, String>> differentialTree = new FigTreeNode<>();
	private final FigTreeNode<String, Map<String, String>> snapshotTree = new FigTreeNode<>();

	public FigProfileModel(StructureDefinition sd) {
		this.sd = sd;

		this.model.put("name", this.getNameFromUrl(sd.getUrl()));
		this.model.put("baseName", this.getNameFromUrl(sd.getUrl()));
		this.model.put("derivation", sd.getDerivation() != null ? sd.getDerivation() : TypeDerivationRule.NULL);
		this.model.put("abstract", sd.getAbstract());

		if (sd.hasSnapshot()) {
			StructureDefinitionSnapshotComponent snapshot = sd.getSnapshot();
			this.createElementTree(snapshot.getElement(), this.snapshotTree);
			this.model.put("snapshot", this.snapshotTree);
		}
		if (sd.hasDifferential()) {
			StructureDefinitionDifferentialComponent differential = sd
					.getDifferential();
			this.createElementTree(differential.getElement(), this.differentialTree);
			this.model.put("differential", this.differentialTree);
		}
	}

	public Map<String, Object> getModel() {
		return this.model;
	}

	public StructureDefinition getSD() {
		return this.sd;
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
			FigTreeNode<String, Map<String, String>> tree) {
		for (ElementDefinition element : elements) {
			String[] keys = this.split(element.getPath());
			Map<String, String> map = new HashMap<>();
			map.put("name", "element");
			map.put("label", keys[keys.length - 1]);
			tree.add(keys, map);
		}

	}

//	public boolean isAbstract() {
//		return this.isAbstract;
//	}
}
