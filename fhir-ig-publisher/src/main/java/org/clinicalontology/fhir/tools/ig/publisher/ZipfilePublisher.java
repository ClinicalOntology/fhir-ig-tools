/**
 *
 */
package org.clinicalontology.fhir.tools.ig.publisher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.clinicalontology.fhir.tools.ig.api.MessageManager;
import org.clinicalontology.fhir.tools.ig.exception.JobRunnerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author dtsteven
 *
 */
@Component
public class ZipfilePublisher {

	@Autowired
	private MessageManager messageManager;

	public void publish(File website) throws JobRunnerException {

		if (!website.isDirectory()) {
			this.messageManager.addError("Website is not a folder: %s", website.getPath());
			return;
		}

		File websiteFile = new File(website.getParentFile(), "website.zip");
		this.messageManager.addInfo("Writing Zip file: %s", websiteFile.getPath());

		try (FileOutputStream fos = new FileOutputStream(websiteFile);
				ZipOutputStream zos = new ZipOutputStream(fos)) {
			zipFile(website, "website", zos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.messageManager.addInfo("Writing Zip file complete.");

	}

	private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {

		if (fileToZip.isDirectory()) {
			zipOut.putNextEntry(new ZipEntry(fileName + "/"));
			zipOut.closeEntry();
			File[] children = fileToZip.listFiles();
			for (File childFile : children) {
				zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
			}
		} else {
			try (FileInputStream fis = new FileInputStream(fileToZip)) {
				ZipEntry zipEntry = new ZipEntry(fileName);
				zipOut.putNextEntry(zipEntry);
				byte[] bytes = new byte[1024];
				int length;
				while ((length = fis.read(bytes)) >= 0) {
					zipOut.write(bytes, 0, length);
				}
			}
		}
	}
}
