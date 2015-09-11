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
import java.net.URL;

import chemaxon.formats.MolExporter;
import chemaxon.formats.MolImporter;
import chemaxon.struc.Molecule;

/**
 * SeekingMolecule is a basic example of seeking for a specific molecule by MolImporter.
 * 
 * @author Erika Biro
 * @version 2012.08.23.
 * @since Marvin 5.11.
 */
public class SeekingMolecule {

	public static void main(String args[]) throws IOException {
		URL url = new URL("http://www.chemaxon.com/marvin/mols-2d/mols.sdf");
		MolImporter importer = new MolImporter(url.openStream(), "sdf");
		importer.seekRecord(2, null);
		Molecule molecule = importer.read();
		System.out.println(MolExporter.exportToFormat(molecule, "mol"));
	}

}
