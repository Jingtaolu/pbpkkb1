/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * ChemAxon. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with ChemAxon.
 *
 */



//import io.myformat.myio.Init;


import java.io.FileOutputStream;
import java.io.FileWriter;

import chemaxon.calculations.clean.Cleaner;
import chemaxon.formats.MFileFormatUtil;
import chemaxon.formats.MolExporter;
import chemaxon.formats.MolImporter;
import chemaxon.struc.Molecule;

/**
 * Writes a file containing two molecules in "myformat".
 *
 * @version Marvin 5.0, 06/04/2007
 * @author  Peter Csizmadia
 */
public class MyExporter {
    public static void main(String[] args) throws Exception {
	// register "myformat"
	MFileFormatUtil.registerFormat(myio.Init.MYFORMAT);

	// create two molecules
        Molecule molecule1 = MolImporter.importMol("CC");
        molecule1.setName("molecule1");
        Cleaner.clean(molecule1, 2, "");
        Molecule molecule2 = MolImporter.importMol("CN1C=NC2=C1C(=O)N(C)C(=O)N2C");
        molecule2.setName("molecule2");
        Cleaner.clean(molecule2, 2, "");
        Molecule molecule3 = MolImporter.importMol("C1=CC=CC=C1");
        molecule3.setName("molecule3");
        Cleaner.clean(molecule3, 2, "");
        
	// write them into test.myf
	FileWriter fw = new FileWriter("test.myf");
	fw.write(MolExporter.exportToFormat(molecule1, "myformat"));
	fw.write(MolExporter.exportToFormat(molecule2, "myformat"));
	fw.write(MolExporter.exportToFormat(molecule3, "myformat"));
	fw.close();

	System.out.println("test.myf created");
    }
}
