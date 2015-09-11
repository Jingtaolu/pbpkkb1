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

import chemaxon.formats.MolImporter;
import chemaxon.struc.MolBond;
import chemaxon.struc.Molecule;
import chemaxon.struc.MoleculeGraph;

/**
 * Example class for aromatization.
 * 
 * @author Janos Kendi
 * 
 */
public class AromatizationExample {

    public static void main(String[] args) throws IOException {

	// Import a molecule from smiles
	Molecule mol = MolImporter.importMol("O=C1NC=CC=C1");
	mol.setDim(0);

	// Call basic aromatization method
	mol.aromatize(MoleculeGraph.AROM_BASIC);
	System.out.println("Aromatic: " + isAromatic(mol));

	// Call general aromatization method
	mol.aromatize(MoleculeGraph.AROM_GENERAL);
	System.out.println("Aromatic: " + isAromatic(mol));
    }

    /**
     * Check if the given molecule is aromatic or not.
     * 
     * @param m
     * @return true if the molecule is aromatic, false otherwise
     */
    public static boolean isAromatic(Molecule m) {
	boolean aromatic = false;
	for (MolBond b : m.getBondArray()) {
	    if (b.getType() == MolBond.AROMATIC) {
		aromatic = true;
		break;
	    }
	}
	return aromatic;
    }
}
