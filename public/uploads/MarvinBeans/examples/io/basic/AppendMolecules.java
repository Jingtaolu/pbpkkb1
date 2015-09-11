package io.basic;

/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * ChemAxon. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with ChemAxon.
 *
 */

import java.io.FileOutputStream;
import java.io.IOException;

import chemaxon.formats.MolExporter;
import chemaxon.formats.MolImporter;
import chemaxon.struc.Molecule;

/**
 * Exporting the connected components of a molecule into a cml structure file.
 * 
 * @author Erika Biro
 * @version 2012.08.23.
 * @since Marvin 5.11.
 */
public class AppendMolecules {
	public static void main(String args[]) throws IOException {
		Molecule molecule = MolImporter
				.importMol("C1CCCC1.C1CCCC1.N1C=CC=C1.C1CCCCC1.C1=CC=CC=C1.C1=CC2=C(C=C1)C=CC=C2");
		FileOutputStream outputStream = new FileOutputStream(
				"fragmentedMolecule.cml");
		MolExporter exporter = new MolExporter(outputStream, "cml");
		Molecule fragments[] = molecule.findFrags(Molecule.class,
				Molecule.FRAG_BASIC);
		for (Molecule fragment : fragments) {
			exporter.write(fragment);
		}
		exporter.close();

	}
}
