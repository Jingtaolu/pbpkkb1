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

import chemaxon.calculations.clean.Cleaner;
import chemaxon.formats.MolExporter;
import chemaxon.formats.MolImporter;
import chemaxon.struc.Molecule;
import chemaxon.struc.RxnMolecule;

/**
 * Example class. Creates a basic RxnMolecule.
 * 
 * @author Janos Kendi
 * 
 */
public class BuildRxnMolecule {

    public static void main(String[] args) throws IOException {

	// Create an empty reaction
	RxnMolecule mol = new RxnMolecule();

	// Create the components
	Molecule reactant1 = MolImporter.importMol("CC(=C)C");
	Molecule reactant2 = MolImporter.importMol("Cl");
	Molecule agent = MolImporter.importMol("CCOCC");
	Molecule product = MolImporter.importMol("C(Cl)(C)(C)C");

	// Add the components
	mol.addComponent(reactant1, RxnMolecule.REACTANTS);
	mol.addComponent(reactant2, RxnMolecule.REACTANTS);
	mol.addComponent(agent, RxnMolecule.AGENTS);
	mol.addComponent(product, RxnMolecule.PRODUCTS);

	// Calculate coordinates.
	Cleaner.clean(mol, 2, null);

	// Change the reaction arrow type.
	mol.setReactionArrowType(RxnMolecule.EQUILIBRIUM);

	System.out.println(MolExporter.exportToFormat(mol, "mrv:P"));
    }
}
