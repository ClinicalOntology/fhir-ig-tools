/**
 *
 */
package org.clinicalontology.fhir.tools.ig;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;

/**
 * @author dtsteven
 *
 */

public class FreemarkerTests {

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
}
