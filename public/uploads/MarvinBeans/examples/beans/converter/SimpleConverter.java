/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * ChemAxon. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with ChemAxon.
 *
 */

import chemaxon.formats.*;
import chemaxon.struc.*;
import java.io.*;
import chemaxon.calculations.clean.CleanerUtil;

/**
 * Simple molecule file converter.
 */
public class SimpleConverter
{
    public static void main(String[] args) throws Exception {
	if(args.length < 2) {
	    System.out.println(
		"Usage: convert input_file format\n"+
		"Example: convert caffeine.mol png");
	    System.exit(0);
	}
	MolImporter importer = new MolImporter(args[0]);
	Molecule mol = importer.read();
	new CleanerUtil().clean(mol, 2, null, null);
	String format = args[1];
	byte[] data = MolExporter.exportToBinFormat(mol, format);
	if(data == null) {
	    System.err.println("Cannot export molecule in \""
				+ format + "\" format.");
	    System.exit(1);
	}
	String outfile = "output."+format;
	OutputStream out = new FileOutputStream(outfile);
	out.write(data);
	out.close();
	System.out.println("Output written to file "+outfile);
	System.exit(0);
    }
}
