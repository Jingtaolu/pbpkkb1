/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * ChemAxon. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with ChemAxon.
 *
 */

import chemaxon.struc.Molecule;
import chemaxon.formats.MolImporter;
import chemaxon.formats.MFileFormatUtil;
import java.io.File;

import chemaxon.formats.MFileFormatUtil;
import chemaxon.formats.MolExporter;
import chemaxon.formats.MolImporter;
import chemaxon.struc.Molecule;

/**
 * Reads a file and prints its contents in SMILES format.
 *
 * @version Marvin 5.0, 06/04/2007
 * @author  Peter Csizmadia
 */
public class MyImporter {
    public static void main(String[] args) throws Exception {
	// register "myformat"
	MFileFormatUtil.registerFormat(myio.Init.MYFORMAT);

	if(args.length == 0) {
	    System.out.println("usage: java -classpath .:<path to MarvinBeans.jar> MyImporter filename");
	    return;
	}

	// create molecule importer for the file specified in the first argument
	MolImporter mri = new MolImporter(new File(args[0]), "myformat");

	// read molecules and print them in SMILES
	Molecule mol;
	while((mol = mri.read()) != null) {
	    System.out.println(MolExporter.exportToFormat(mol, "mrv:P"));
	}
    }
}
