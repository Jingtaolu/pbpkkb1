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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import chemaxon.formats.MolExporter;
import chemaxon.formats.MolImporter;
import chemaxon.struc.Molecule;

/**
 * Exporting reaction molecule with registry number into  rdf structure file.
 * 
 * @author Erika Biro
 * @version 2012.08.23.
 * @since Marvin 5.11.
 */
public class ExportingReactions {
	public static void main(String args[]) throws IOException {
		MolImporter imp = new MolImporter(new FileInputStream("examples/io/basic/mols.rdf"), "rdf");
		MolExporter exp = new MolExporter(new FileOutputStream(
				"outputMolecules.rdf"), "rdf");
		Molecule mol;
		int regno = 218;
		while ((mol = imp.read()) != null) {
			// add the internal registry number (sequence number in the
			// database) of the reaction
			mol.setProperty("$REGNO", "RI" + regno);
			regno++;
			exp.write(mol);

		}
		imp.close();
		exp.close();
	}
}
