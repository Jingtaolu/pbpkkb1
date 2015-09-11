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

import chemaxon.formats.MolConverter;
import chemaxon.formats.MolExporter;

/**
 * Converting simple sdf format to SMILES format
 * 
 * @author Erika Biro
 * @version 2012.08.23.
 * @since Marvin 5.11.
 */

public class MoleculeConverter {
	public static void main(String args[]) throws IOException {
		// Simple SDfile to SMILES conversion:
		MolConverter.Builder mcbld = new MolConverter.Builder();
		mcbld.addInput("examples/plugin-api/example_mols.sdf", "");
		mcbld.setOutput("out.smiles", "smiles");
		mcbld.setOutputFlags(MolExporter.TEXT);
		MolConverter mc = mcbld.build();
		boolean done=false;
		while (!done) {
			done=mc.convert();
		}
		mc.close();
	}
}
