/*
 *  Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 *  This software is the confidential and proprietary information of
 *  ChemAxon. You shall not disclose such Confidential Information
 *  and shall use it only in accordance with the terms of the agreements
 *  you entered into with ChemAxon.
 *  
 */
package core;

import java.io.IOException;

import chemaxon.calculations.ElementalAnalyser;
import chemaxon.calculations.clean.Cleaner;
import chemaxon.formats.MolExporter;
import chemaxon.formats.MolImporter;
import chemaxon.marvin.util.CleanUtil;
import chemaxon.struc.Molecule;
import chemaxon.struc.Sgroup;
import chemaxon.struc.graphics.MBracket;
import chemaxon.struc.sgroup.RepeatingUnitSgroup;
import chemaxon.struc.sgroup.SuperatomSgroup;

/**
 * Example class for creating sgroups.
 * 
 * @author Janos Kendi
 * 
 */
public class BuildMoleculeWithSgroupExample {

    public static void main(String[] args) throws IOException {

	// Import the molecule
	Molecule mol = MolImporter.importMol("C1=CC=CC=C1C(C)CC");
	Cleaner.clean(mol, 2, null);

	// Create the Sgroups.
	SuperatomSgroup superSg = new SuperatomSgroup(mol);
	RepeatingUnitSgroup repeatingSg = new RepeatingUnitSgroup(mol, "ht",
		Sgroup.ST_SRU);
	repeatingSg.setConnectivity(Sgroup.SCN_HEAD_TO_HEAD);
	
	superSg.setSubscript("Ph");
	
	// Set the parent-child relation.
	repeatingSg.addChildSgroup(superSg);
	for (int i = 0; i < 6; i++) {
	    mol.setSgroupParent(mol.getAtom(i), superSg, true);
	}

	// Set the RepeatingUnitSgroup atoms.
	for (int i = 0; i < 9; i++) {
	    if (mol.getAtom(i).getBondCount() > 1) {
		mol.setSgroupParent(mol.getAtom(i), repeatingSg, true);
	    }
	}
	repeatingSg.addStarAtoms();
	repeatingSg.setSubscript("4");
	
	CleanUtil.generateBracketCoords(repeatingSg, MBracket.T_SQUARE);
	
	ElementalAnalyser elem = new ElementalAnalyser();
	elem.setMolecule(mol);
	System.out.println(elem.dotDisconnectedFormula());

	System.out.println(MolExporter.exportToFormat(mol, "mrv:P"));
    }

}
