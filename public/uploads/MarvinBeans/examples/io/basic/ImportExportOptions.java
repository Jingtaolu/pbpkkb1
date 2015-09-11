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

import java.io.FileInputStream;
import java.io.IOException;

import chemaxon.calculations.clean.Cleaner;
import chemaxon.formats.MolExporter;
import chemaxon.formats.MolFormatException;
import chemaxon.formats.MolImporter;
import chemaxon.struc.Molecule;

/**
 * The ImportExportOptions example demonstrates the usage of format options,
 * general and format dependent options of MolImporter and MolExporter classes.
 * 
 * @author Erika Biro
 * @version 2012.08.23.
 * @since Marvin 5.11.
 */
public class ImportExportOptions {

	public static void main(String args[]) {
		try {
		    // Importing a multi-molecule sdf file. The "MULTISET" option merges 
		    // the multiple molecules to one Molecule object, the "Usg" ungroups the
		    // S-groups in them.
			MolImporter importer = new MolImporter(new FileInputStream("examples/io/basic/mols.sdf"), "sdf:MULTISET,Usg");
			Molecule molecule = importer.read();
			Cleaner.clean(molecule, 2, null);
			importer.close();
			// Exporting the molecule to mol V3000 format and removing explicit Hydrogens. 
			MolExporter exporter = new MolExporter("outputMolecule.mol",
					"mol:V3,-H");
			exporter.write(molecule);
			exporter.close();

		} catch (MolFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
