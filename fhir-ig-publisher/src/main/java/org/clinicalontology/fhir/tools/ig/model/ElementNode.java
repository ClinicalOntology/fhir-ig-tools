/**
 *
 */
package org.clinicalontology.fhir.tools.ig.model;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.ElementDefinition;

import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleSequence;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNodeModel;
import freemarker.template.TemplateSequenceModel;

/**
 * @author dtsteven
 *
 */
public class ElementNode implements TemplateNodeModel, TemplateHashModel {

	private ElementDefinition payload;
	private List<ElementNode> children = new ArrayList<>();
	private SimpleSequence childrenTemplate;

	private final ElementNode parent;
	private final String key;
	private final ObjectWrapper wrapper;

	public ElementNode(ObjectWrapper wrapper) throws TemplateModelException {
		this(wrapper, null, null, null);
	}

	private ElementNode(ObjectWrapper wrapper, ElementDefinition payload, String key, ElementNode parent) {
		this.wrapper = wrapper;
		this.payload = payload;
		this.parent = parent;
		this.key = key;
		// this.childrenTemplate = new SimpleSequence(this.children, wrapper);
	}

	public ElementNode add(String key) {
		return this.add(key, null);
	}

	/**
	 * traverse the tree. If intermediate key is not found, then create with an
	 * empty payload. Always create the last node
	 *
	 * @param keys
	 * @param payload
	 * @return
	 */
	public ElementNode add(String[] keys, ElementDefinition payload) {

		if (keys == null || keys.length == 0) {
			throw new IllegalArgumentException("Empty keys[] argument");
		}
		int last = keys.length - 1;
		ElementNode curr = this;
		for (int i = 0; i < last; i++) {
			ElementNode next = curr.find(keys[i]);
			if (next == null) {
				next = curr.add(keys[i]);
			}
			curr = next;
		}
		return curr.add(keys[last], payload);
	}

	/**
	 * always add a new key, even if a duplicate
	 *
	 * @param key
	 * @param payload
	 * @return
	 */
	public ElementNode add(String key, ElementDefinition payload) {

		ElementNode childNode = new ElementNode(this.wrapper, payload, key, this);
		this.children.add(childNode);
		return childNode;
	}

	private ElementNode find(String key) {
		for (ElementNode node : this.children) {
			if (key.equals(node.key)) {
				return node;
			}
		}
		return null;
	}

	@Override
	public TemplateModel get(String key) throws TemplateModelException {

		switch (key) {
		case "title":
		case "name":
			return this.wrapper.wrap(this.key);
		case "children":
			return this.getChildNodes();
		default:
			throw new TemplateModelException("Unknown Hash for Get: " + key);
		}
	}

	@Override
	public TemplateNodeModel getParentNode() throws TemplateModelException {
		return this.parent;
	}

	@Override
	public TemplateSequenceModel getChildNodes() throws TemplateModelException {
		if (this.childrenTemplate == null) {
			this.childrenTemplate = new SimpleSequence(this.children, this.wrapper);
		}
		return this.childrenTemplate;
	}

	@Override
	public String getNodeName() throws TemplateModelException {
		return "element";
	}

	@Override
	public String getNodeType() throws TemplateModelException {
		return "element";
	}

	@Override
	public String getNodeNamespace() throws TemplateModelException {
		return null;
	}

	@Override
	public boolean isEmpty() throws TemplateModelException {
		// TODO Auto-generated method stub
		return false;
	}
}