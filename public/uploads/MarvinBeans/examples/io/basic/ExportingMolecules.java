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

import chemaxon.calculations.clean.Cleaner;
import chemaxon.formats.MolExporter;
import chemaxon.formats.MolFormatException;
import chemaxon.formats.MolImporter;
import chemaxon.struc.MolAtom;
import chemaxon.struc.MolBond;
import chemaxon.struc.Molecule;
import chemaxon.struc.RgMolecule;

/**
 * Exporting different kind of Molecules (simple and R-groups) using two formats (mrv and smiles).
 * 
 * @author Erika Biro
 * @version 2012.08.23.
 * @since Marvin 5.11.
 */

public class ExportingMolecules {

	/**Builds a simple molecule.
	 * 
	 * @return the created molecule
	 */
	public static Molecule buildSimpleMolecule() {

		// create an empty Molecule
		Molecule molecule = new Molecule();

		// create the Carbon atom
		MolAtom atom1 = new MolAtom(6);
		// and add it to the molecule
		molecule.add(atom1);

		// create the Hydrogen atom
		MolAtom atom2 = new MolAtom(1);
		// and add it to the molecule
		molecule.add(atom2);

		// create the Hydrogen atom
		MolAtom atom3 = new MolAtom(1);
		// and add it to the molecule
		molecule.add(atom3);

		// create a bond between atoms, add the created bond to the molecule
		MolBond bond1 = new MolBond(atom1, atom2, 1);
		molecule.add(bond1);
		MolBond bond2 = new MolBond(atom1, atom3, 1);
		molecule.add(bond2);
		
		// Clean the molecule to generate 2D coordinates
		Cleaner.clean(molecule, 2, null);

		return molecule;
	}

	/**
	 * Builds a molecule with root and R-group definitions.
	 * 
	 * @return the created molecule
	 *
	 */
	private static RgMolecule buildRgMolecule() throws MolFormatException {

		// Create the root of the RgMolecule
		Molecule root = MolImporter.importMol("C1CCCCC1");

		// Create Rgroups
		MolAtom r1 = new MolAtom(MolAtom.RGROUP);
		r1.setRgroup(1);
		root.add(r1);
		root.add(new MolBond(r1, root.getAtom(0)));

		MolAtom r2 = new MolAtom(MolAtom.RGROUP);
		r2.setRgroup(2);
		root.add(r2);
		root.add(new MolBond(r2, root.getAtom(5)));

		// Create the RgMolecule
		RgMolecule rgMol = new RgMolecule();
		rgMol.setRoot(root);

		// Add Rgroup definitions
		Molecule rg = MolImporter.importMol("O");
		rg.getAtom(0).addRgroupAttachmentPoint(1, 1);
		rgMol.addRgroup(1, rg);

		rg = MolImporter.importMol("N");
		rg.getAtom(0).addRgroupAttachmentPoint(1, 2);
		rgMol.addRgroup(1, rg);

		rg = MolImporter.importMol("CC");
		rg.getAtom(0).addRgroupAttachmentPoint(1, 1);
		rgMol.addRgroup(2, rg);

		// Clean the molecule to generate 2D coordinates
		Cleaner.clean(rgMol, 2, null);

		return rgMol;
	}

	public static void main(String args[]) throws IOException {
		Molecule molecule = buildSimpleMolecule();
		System.out.println("Water molecule in mrv: ");
		System.out.println(MolExporter.exportToFormat(molecule, "mrv:P"));
		System.out.println("Water molecule in smiles: ");
		System.out.println(MolExporter.exportToFormat(molecule, "smiles"));
		RgMolecule rgMol = buildRgMolecule();
		System.out.println("Molecule with R-groups in mrv: ");
		System.out.println(MolExporter.exportToFormat(rgMol, "mrv:P"));
		System.out.println("Molecule with R-groups in smiles: ");
		System.out.println(MolExporter.exportToFormat(rgMol, "smiles"));
	}

}
