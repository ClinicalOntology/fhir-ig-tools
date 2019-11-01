/**
 *
 */
package org.clinicalontology.fhir.tools.ig;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNodeModel;
import freemarker.template.TemplateNotFoundException;
import freemarker.template.TemplateSequenceModel;

/**
 * @author dtsteven
 *
 */

public class FreemarkerTests {

	public static class content implements TemplateNodeModel {
		private String name;
		private String text;

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getText() {
			return this.text;
		}

		public void setText(String text) {
			this.text = text;
		}

		@Override
		public TemplateNodeModel getParentNode() throws TemplateModelException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public TemplateSequenceModel getChildNodes() throws TemplateModelException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getNodeName() throws TemplateModelException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getNodeType() throws TemplateModelException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getNodeNamespace() throws TemplateModelException {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private static final String myTestTemplate = "<html>\r\n" +
			"<head>\r\n" +
			"  <title>Welcome!</title>\r\n" +
			"</head>\r\n" +
			"<body>\r\n" +
			"  <h1>Welcome ${user}!</h1>\r\n" +
			"  <p>Our latest product:\r\n" +
			"  <a href=\"${latestProduct.url}\">${latestProduct.name}</a>!\r\n" +
			"</body>\r\n" +
			"</html>";

	private static Configuration cfg;

	private Map<String, Object> root;

	public static class Product {

		private String url;
		private String name;

		// As per the JavaBeans spec., this defines the "url" bean property
		// It must be public!
		public String getUrl() {
			return this.url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		// As per the JavaBean spec., this defines the "name" bean property
		// It must be public!
		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	@BeforeClass
	public static void classInit() throws IOException {

		StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
		stringTemplateLoader.putTemplate("myTestTemplate", myTestTemplate);
		stringTemplateLoader.putTemplate("myRecurseTemplate", myRecurseTemplate);

		// Create your Configuration instance, and specify if up to what FreeMarker
		// version (here 2.3.29) do you want to apply the fixes that are not 100%
		// backward-compatible. See the Configuration JavaDoc for details.
		cfg = new Configuration(Configuration.VERSION_2_3_29);

		cfg.setTemplateLoader(stringTemplateLoader);

		// From here we will set the settings recommended for new projects. These
		// aren't the defaults for backward compatibilty.

		// Set the preferred charset template files are stored in. UTF-8 is
		// a good choice in most applications:
		cfg.setDefaultEncoding("UTF-8");

		// Sets how errors will appear.
		// During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is
		// better.
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		// Don't log exceptions inside FreeMarker that it will thrown at you anyway:
		cfg.setLogTemplateExceptions(false);

		// Wrap unchecked exceptions thrown during template processing into
		// TemplateException-s:
		cfg.setWrapUncheckedExceptions(true);

		// Do not fall back to higher scopes when reading a null loop variable:
		cfg.setFallbackOnNullLoopVariable(false);
	}

	@Before
	public void init() {
		this.root = new HashMap<>();

		// Put string "user" into the root
		this.root.put("user", "Big Joe");

		// Create the "latestProduct" hash. We use a JavaBean here, but it could be a
		// Map too.
		Product latest = new Product();
		latest.setUrl("products/greenmouse.html");
		latest.setName("green mouse");
		// and put it into the root
		this.root.put("latestProduct", latest);
	}

	@Test
	public void simpleFreemarkerTest() throws TemplateNotFoundException,
			MalformedTemplateNameException, ParseException, IOException, TemplateException {
		Template temp = cfg.getTemplate("myTestTemplate");

		Writer out = new OutputStreamWriter(System.out);
		temp.process(this.root, out);
	}

	private static final String xmlDocument = "<book>\r\n" +
			"  <title>Test Book</title>\r\n" +
			"  <chapter>\r\n" +
			"    <title>Ch1</title>\r\n" +
			"    <para>p1.1</para>\r\n" +
			"    <para>p1.2</para>\r\n" +
			"    <para>p1.3</para>\r\n" +
			"  </chapter>\r\n" +
			"  <chapter>\r\n" +
			"    <title>Ch2</title>\r\n" +
			"    <para>p2.1</para>\r\n" +
			"    <para>p2.2</para>\r\n" +
			"  </chapter>\r\n" +
			"</book>";

	private static final String myRecurseTemplate = "<#recurse doc>\r\n" +
			"\r\n" +
			"<#macro book>\r\n" +
			"  Book element with title ${.node.title}\r\n" +
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

	private HashMap<String, Object> doc;

	@Before
	public void initXmlDoc() throws SAXException, IOException, ParserConfigurationException {

		InputSource inputSource = new InputSource(new StringReader(xmlDocument));
		this.doc = new HashMap<>();
		this.doc.put(
				"doc",
				freemarker.ext.dom.NodeModel.parse(inputSource));
	}

	@Test
	public void recurseFreemarkerTest() throws TemplateNotFoundException,
			MalformedTemplateNameException, ParseException, IOException, TemplateException {

		Template temp = cfg.getTemplate("myRecurseTemplate");

		Writer out = new OutputStreamWriter(System.out);
		temp.process(this.doc, out);

	}
}
