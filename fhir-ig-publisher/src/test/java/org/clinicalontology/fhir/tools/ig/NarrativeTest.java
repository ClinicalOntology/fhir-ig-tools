/**
 *
 */
package org.clinicalontology.fhir.tools.ig;

import org.hl7.fhir.r4.model.Patient;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;

/**
 * @author dtsteven
 *
 */
public class NarrativeTest {

//	private static FhirContext ctx;
//	private Patient patient;

	@BeforeClass
	public static void initNarrative() {
//		ctx = FhirContext.forDstu2();
//		ctx.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

	}

	@Before
	public void init() {
//		this.patient = new Patient();
//		this.patient.addIdentifier().setSystem("urn:foo").setValue("7000135");
//		this.patient.addName().setFamily("Smith").addGiven("John").addGiven("Edward");
//		this.patient.addAddress().addLine("742 Evergreen Terrace").setCity("Springfield").setState(
//				"ZZ");
	}

	@Test
	public void narrativeTest() {
		FhirContext ctx = FhirContext.forR4();
		// ctx.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:foo").setValue("7000135");
		patient.addName().setFamily("Smith").addGiven("John").addGiven("Edward");
		patient.addAddress().addLine("742 Evergreen Terrace").setCity("Springfield").setState(
				"ZZ");

		System.err.printf("In narrativeTest\n");
		String output = ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(
				patient);
		System.err.println(output);
	}
}
