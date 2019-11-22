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
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * @author dtsteven
 *
 */
public class FreemarkerMacroTests {

	static public class Foo {
		private final String bar;
		private List<Foo> foos = new ArrayList<>();

		public Foo(String payload) {
			this.bar = payload;
		}

		public String getBar() {
			return this.bar;
		}

		public List<Foo> getFoos() {
			return this.foos;
		}

	};

	private static Configuration cfg;

	private static final String myTestTemplate = "<#macro dumpFoo foo>\r\n" +
			"  <ul>${foo.bar}\r\n" +
			"  <#list foo.foos as childFoo>\r\n" +
			"   -- <li><@dumpFoo childFoo /> </li>\r\n" +
			"  </#list>\r\n" +
			"  </ul>\r\n" +
			"</#macro>\r\n" +
			"\r\n" +
			"<@dumpFoo myFoo />";

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
	public void basicMacroTest() throws TemplateException, IOException {
		Foo myFoo = new Foo("root");
		Foo child1 = new Foo("child 1");
		Foo child11 = new Foo("child 1.1");
		Foo child111 = new Foo("child 1.1.1");
		child11.getFoos().add(child111);
		child1.getFoos().add(child11);

		Foo child2 = new Foo("child 2");
		myFoo.getFoos().add(child1);
		myFoo.getFoos().add(child2);

		Map<String, Object> root = new HashMap<>();

		// Put string "user" into the root
		root.put("myFoo", myFoo);

		Template temp = cfg.getTemplate("myTestTemplate");

		Writer out = new OutputStreamWriter(System.out);
		temp.process(root, out);

	}
}
