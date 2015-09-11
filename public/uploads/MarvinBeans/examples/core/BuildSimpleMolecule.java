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
import chemaxon.calculations.hydrogenize.Hydrogenize;
import chemaxon.formats.MolExporter;
import chemaxon.struc.MolAtom;
import chemaxon.struc.MolBond;
import chemaxon.struc.Molecule;
import chemaxon.struc.PeriodicSystem;

/**
 * Example class. Creates a simple molecule.
 * 
 * @author Janos Kendi
 * 
 */
public class BuildSimpleMolecule {

    public static void main(String[] args) throws IOException {

	// Create an empty molecule
	Molecule mol = new Molecule();

	// Create atoms
	MolAtom a1 = new MolAtom(6);
	mol.add(a1);

	MolAtom a2 = new MolAtom(PeriodicSystem.O);
	mol.add(a2);

	// Create bond
	MolBond b1 = new MolBond(a1, a2, 2);
	mol.add(b1);

	// Calculate coordinates

	mol.setDim(0);
	Cleaner.clean(mol, 2, null);

	// Call valence check
	mol.valenceCheck();

	// Add and remove H atoms
	Hydrogenize.convertImplicitHToExplicit(mol);
	Hydrogenize.convertExplicitHToImplicit(mol, MolAtom.ALL_H);

	// Check the implicit and explicit H count of the atoms.
	System.out.println("C - implicit H : " + a1.getImplicitHcount());
	System.out.println("C - explicit H : " + a1.getExplicitHcount());
	System.out.println("O - implicit H : " + a2.getImplicitHcount());
	System.out.println("O - explicit H : " + a2.getExplicitHcount());

	System.out.println(MolExporter.exportToFormat(mol, "mrv:P"));

    }
}
