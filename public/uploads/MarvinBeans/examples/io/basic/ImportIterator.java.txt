/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * ChemAxon. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with ChemAxon.
 *
 */

package io.basic;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import chemaxon.formats.MolFormatException;
import chemaxon.formats.MolImporter;
import chemaxon.struc.Molecule;

/**
 * ImportIterator is a basic example of iterating on molecules imported by MolImporter
 * @author Erika Biro
 * @version 2012.08.23.
 * @since Marvin 5.11.
 */
public class ImportIterator {

    public static void main(String args[]) {
	URL url = null;
	try {
		url = new URL("http://www.chemaxon.com/marvin/mols-2d/mols.sdf");
	} catch (MalformedURLException e) {
		e.printStackTrace();
	}
	try {
		for (Molecule molecule : new MolImporter(url.openStream())) {
		    System.out.println(molecule.getAtomCount());
		}
	} catch (MolFormatException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
    }
}
