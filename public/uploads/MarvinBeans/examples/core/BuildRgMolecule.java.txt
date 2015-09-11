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
import chemaxon.struc.MolAtom;
import chemaxon.struc.MolBond;
import chemaxon.struc.Molecule;
import chemaxon.struc.RgMolecule;

/**
 * Example class. Creates a basic RgMolecule.
 * 
 * @author Janos Kendi
 * 
 */
public class BuildRgMolecule {

    public static void main(String[] args) throws IOException {

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
	rg.getAtom(0).addRgroupAttachmentPoint(2, 1);
	rgMol.addRgroup(1, rg);

	rg = MolImporter.importMol("N");
	rg.getAtom(0).addRgroupAttachmentPoint(1, 1);
	rgMol.addRgroup(1, rg);

	rg = MolImporter.importMol("CC");
	rg.getAtom(0).addRgroupAttachmentPoint(1, 1);
	rgMol.addRgroup(2, rg);

	Cleaner.clean(rgMol, 2, null);

	System.out.println(MolExporter.exportToFormat(rgMol, "mrv:P"));
    }

}
