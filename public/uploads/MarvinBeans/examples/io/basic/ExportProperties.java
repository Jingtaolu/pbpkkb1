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

import java.io.IOException;

import chemaxon.formats.MolExporter;
import chemaxon.formats.MolImporter;
import chemaxon.struc.Molecule;
import chemaxon.struc.prop.MHashProp;
import chemaxon.struc.prop.MMoleculeProp;
import chemaxon.struc.prop.MStringProp;

/**
 * Exporting simple properties into rdf structure file.
 * 
 * @author Erika Biro
 * @version 2012.08.23.
 * @since Marvin 5.11.
 */

public class ExportProperties {
	public static void main(String args[]) throws IOException {
		Molecule mol = MolImporter.importMol("ccccc");
		mol.clearProperties();
		MHashProp hashProp = new MHashProp();
		mol.setPropertyObject("ROOT", hashProp);
		hashProp.put("CdId", new MStringProp("1"));
		hashProp.put("Formula", new MStringProp("C5H10"));
		MMoleculeProp secondaryStruc = new MMoleculeProp(
				MolImporter.importMol("cc"));
		hashProp.put("secondStruct", secondaryStruc);
		mol.setProperty("$REGNO", "MI24");
		System.out.println(MolExporter.exportToFormat(mol, "rdf"));
	}
}
