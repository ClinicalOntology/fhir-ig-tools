/**
 *
 */
package org.clinicalontology.fhir.tools.common.util;

import org.clinicalontology.fhir.tools.ig.common.util.CommonUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author dtsteven
 *
 */
public class CommonUtilsTest {

	@Test
	public void castTest() {
		NullPointerException npe = new NullPointerException("Null pointer exception");

		CommonUtils.cast(RuntimeException.class, npe);

		Assert.assertTrue(true);
	}

	@Test(expected = RuntimeException.class)
	public void castFailTest() {
		Integer integer = 123;

		CommonUtils.cast(String.class, integer);

	}
}
