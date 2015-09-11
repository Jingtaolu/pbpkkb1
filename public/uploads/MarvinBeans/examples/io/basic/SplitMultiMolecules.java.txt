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
import java.net.URL;

import chemaxon.formats.MolConverter;
import chemaxon.formats.MolExporter;
import chemaxon.formats.MolImporter;
import chemaxon.struc.Molecule;

/**
 * Example to split a multi-molecule input file into multiple molecule files.
 * 
 * @author Erika Biro
 * @version 2012.08.23.
 * @since Marvin 5.11.
 */
public class SplitMultiMolecules {
	public static void main(String args[]) throws IOException {
		// To split a multi-molecule input file into multiple Molfiles named
		// out1.mol, out2.mol etc., use the following settings:
		MolConverter.Builder mcbld = new MolConverter.Builder();
		mcbld.addInput("examples/plugin-api/example_mols.sdf", "");
		mcbld.setOutput("out.mol", "mol");
		mcbld.setOutputFlags(MolExporter.TEXT | MolExporter.MULTIPLE);
		MolConverter mc = mcbld.build();
		boolean done=false;
		while (!done) {
			done=mc.convert();
		}
		mc.close();
	}
}
