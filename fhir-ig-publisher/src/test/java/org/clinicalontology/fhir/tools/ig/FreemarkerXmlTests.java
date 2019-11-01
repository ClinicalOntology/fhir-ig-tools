/**
 *
 */
package org.clinicalontology.fhir.tools.ig;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.SimpleSequence;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNodeModel;
import freemarker.template.TemplateSequenceModel;

/**
 * @author dtsteven
 *
 */
public class FreemarkerXmlTests {

	static class Element implements TemplateNodeModel, TemplateHashModel {
		private final String name;
		private final String text;
		private Element parent;
		private final List<Element> elements = new ArrayList<>();

		public Element(String name) {
			this(name, null);
		}

		public Element(String name, String text) {
			this.name = name;
			this.text = text;
		}

		public void add(Element element) {
			element.parent = this;
			this.elements.add(element);
		}

		public List<Element> getElements() {
			return this.elements;
		}

		public String getName() {
			return this.name;
		}

		public String getText() {
			return this.text;
		}

		public String getTitle() {
			return this.name;
		}

		@Override
		public TemplateModel get(String key) throws TemplateModelException {

			switch (key) {
			case "title":
			case "name":
				return cfg.getObjectWrapper().wrap(this.name);

			default:
				throw new TemplateModelException("unknown hash get: " + key);
			}
		}

		@Override
		public TemplateNodeModel getParentNode() throws TemplateModelException {
			return this.parent;
		}

		@Override
		public TemplateSequenceModel getChildNodes() throws TemplateModelException {
			// TODO Auto-generated method stub
			return new SimpleSequence(this.elements, cfg.getObjectWrapper());
		}

		@Override
		public String getNodeName() throws TemplateModelException {
			return this.name;
		}

		@Override
		public String getNodeType() throws TemplateModelException {
			return this.name;
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

	private static Configuration cfg;
	private static final String myTestTemplate = "<#recurse doc>\r\n" +
			"\r\n" +
			"<#macro book>\r\n" +
			"  Book element with title ${.node.title} \r\n" +
			"    <#recurse>\r\n" +
			"  End book\r\n" +
			"</#macro>\r\n" +
			"\r\n" +
			"<#macro title>\r\n" +
			"  Title element\r\n" +
			"</#macro>\r\n" +
			"\r\n" +
			"<#macro chapter>\r\n" +
			"  Chapter element with title: ${.node.title}\r\n" +
			"</#macro>";

	@BeforeClass
	public static void classInit() throws IOException {

		StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
		stringTemplateLoader.putTemplate("myTestTemplate", myTestTemplate);

		cfg = new Configuration(Configuration.VERSION_2_3_29);
		cfg.setTemplateLoader(stringTemplateLoader);
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setLogTemplateExceptions(false);
		cfg.setWrapUncheckedExceptions(true);
		cfg.setFallbackOnNullLoopVariable(false);
	}

	@Test
	public void basicXmlTest() throws TemplateException, IOException {

		Element doc = new Element("doc");

		Element book = new Element("book");
		book.add(new Element("title", "Test Book"));
		doc.add(book);

		Element chapter1 = new Element("chapter");
		chapter1.add(new Element("title", "Ch1"));
		chapter1.add(new Element("para", "p1.1"));
		chapter1.add(new Element("para", "p1.2"));
		chapter1.add(new Element("para", "p1.3"));
		book.add(chapter1);

		Element chapter2 = new Element("chapter");
		chapter2.add(new Element("title", "Ch2"));
		chapter2.add(new Element("para", "p2.1"));
		chapter2.add(new Element("para", "p2.2"));
		chapter2.add(new Element("para", "p2.3"));
		book.add(chapter2);

		Map<String, Object> root = new HashMap<>();
		// Put string "user" into the root
		root.put("doc", doc);

		Template temp = cfg.getTemplate("myTestTemplate");

		Writer out = new OutputStreamWriter(System.out);
		temp.process(root, out);

	}

}
