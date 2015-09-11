/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * ChemAxon. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with ChemAxon.
 *
 */

package naming;

import java.io.File;

import chemaxon.marvin.io.MolExportException;
import chemaxon.naming.DocumentExtractor;
import chemaxon.naming.DocumentExtractor.Hit;
import chemaxon.naming.DocumentExtractor.ProgressInfo;
import chemaxon.naming.DocumentExtractor.ProgressListener;

public class DocumentExtractorListenerExample {
    
    /**
     * Expects the name of a plain text file as the first argument
     * The list of hits is printed on the standard output.
     */
    public static void main(String[] args) throws Exception {
	ProgressListener listener = new ProgressListener() {
	    public boolean progress(ProgressInfo info) {
		System.out.println("PROGRESS: " + info.getCharactersRead() + "/" + info.getEstimatedTotalCharacters());
		// Return true if you want to stop the processing
		return false;
	    }
	};
	
	String inFileName = args[0];
	DocumentExtractor x = new DocumentExtractor(new File(inFileName));

	x.processPlainText(listener);

	for (Hit hit : x.getHits()) {
	    try {
		System.out.println(hit.position + ": " + hit.text + ": " + hit.structure.exportToFormat("cxsmiles"));
	    }
	    catch (MolExportException e) {
		System.err.println("Error exporting structure for '" + hit.text + "': " + e);
	    }
	}
    }
}
